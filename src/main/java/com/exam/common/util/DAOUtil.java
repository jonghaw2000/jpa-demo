package com.exam.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;

import com.exam.common.IQueryGenerator;

/**
 * Generic DAO 에서 필요한 utility 들
 * @author uchung
 *
 */
abstract public class DAOUtil {
  /**
   * argument 들을 다수 넣고 싶을 때
   */
  public static final String ARGUMENTS_ARG_NAME = ":arguments";
  /**
   * equals operator
   */
  public static final int EQ = 0;
  /**
   * not equals operator
   */
  public static final int NE = 1;
  /**
   * greater than operator
   */
  public static final int GT = 2;
  /**
   * greater than or equal to operator
   */
  public static final int GTE = 3;
  /**
   * less than operator
   */
  public static final int LT = 4;
  /**
   * less than or equal to operator
   */
  public static final int LTE = 5;
  /**
   * LIKE operator
   */
  public static final int LIKE = 6;

  private static final String [] OPERATOR_STR = 
  {null, "<>", ">", ">=", "<", "<=", " LIKE"};
  private static final Pattern NAMED_PARAM = Pattern.compile("\\:\\w+");

  static final String LPAREN = "(";
  static final String _AND_ = " AND ";
  static final String _OR_ = " OR ";
  static final String _WHERE_ = " WHERE ";
  private static final int WHERE_LEN = _WHERE_.length();
  private DAOUtil() {}
  
  /**
   * Hibernate Query 객체를 생성함
   * @param session 사용할 Hibernate session
   * @param queryString 지금까지 모아온 HQL
   * @param start paging 시 첫 row
   * @param limit paging 시 page size
   * @param orderBy order by
   * @param cacheRegion 사용할 cache region
   * @param parameters parameter 들
   * @return Query 객체
   */
  public static final Query createQuery(EntityManager entityManager, StringBuilder queryString,
      int start, int limit, String orderBy, String cacheRegion, List<Object> parameters) {
    return createQuery(entityManager, queryString, start, limit, orderBy, null, cacheRegion, parameters);
  }
  /**
   * Hibernate Query 객체를 생성함
   * @param session 사용할 Hibernate session
   * @param queryString 지금까지 모아온 HQL
   * @param start paging 시 첫 row
   * @param limit paging 시 page size
   * @param orderBy order by
   * @param groupBy 사용할 group by statement 들
   * @param cacheRegion 사용할 cache region
   * @param parameters parameter 들
   * @return Query 객체
   */
  public static final Query createQuery(EntityManager entityManager, StringBuilder queryString,
      int start, int limit, String orderBy, String groupBy, String cacheRegion, List<Object> parameters) {
    int len = queryString.length();
    if (_WHERE_.equals(queryString.substring(len - WHERE_LEN))) {
      queryString.setLength(len - WHERE_LEN);
    } else if (_AND_.equals(queryString.substring(len - _AND_.length()))) {
      queryString.setLength(len - _AND_.length());
    }
    
    if (groupBy != null) {
      queryString.append(" GROUP BY ").append(groupBy);
    }
    addOrderBy(queryString, orderBy);
    Query q = entityManager.createQuery(queryString.toString());
    if (start > 0) {
      q.setFirstResult(start);
    }
    if (limit > 0) {
      q.setMaxResults(limit);
    }
    if (cacheRegion != null) {
//      q.setCacheable(true);
//      q.setCacheRegion(cacheRegion);
      q.setHint("org.hibernate.cacheable", true);
      q.setHint("org.hibernate.cacheMode", CacheMode.NORMAL);
      q.setHint("org.hibernate.cacheRegion", cacheRegion); // region 지정
    }
    setParameter(q, parameters);
    return q;
  }
  
  static final void addOrderBy(StringBuilder queryString, String orderBy) {
    if (orderBy != null) {
      queryString.append(" ORDER BY ").append(orderBy);
    }
  }
  
  /**
   * entityClass 에 해당되는 HQL 을 return 함
   * @param entityClass 이 entity class 를 select 함
   * @param selectedFields select 할 field 들
   * @return entityClass 에 해당되는 HQL 을 return 함
   */
  public static final StringBuilder createQueryString(Class<?> entityClass,
      String... selectedFields) {
    return applyProjections(entityClass.getSimpleName(), null, selectedFields);
  }
  
  /**
   * entityName 에 해당되는 HQL 을 return 함
   * @param entityName 이 entity 종류를 select 함
   * @param selectedFields select 할 field 들
   * @return entityName 에 해당되는 HQL 을 return 함
   */
  public static final StringBuilder createQueryString(String entityName,
      String... selectedFields) {
    return applyProjections(entityName, null, selectedFields);
  }  
  /**
   * entityClass 에 해당되는 HQL 을 return 함
   * @param entityClass 이 entity class 를 select 함
   * @param joins join statement 들
   * @param selectedFields select 할 field 들
   * @return entityClass 에 해당되는 HQL 을 return 함
   */
  public static final StringBuilder createQueryStringWithJoins(Class<?> entityClass,
      String joins, String... selectedFields) {
    return applyProjections(entityClass.getSimpleName(), joins, selectedFields);
  }
  /**
   * entityName 에 해당되는 HQL 을 return 함
   * @param entityName 이 entity 종류를 select 함
   * @param joins join statement 들
   * @param selectedFields select 할 field 들
   * @return entityName 에 해당되는 HQL 을 return 함
   */
  public static final StringBuilder createQueryStringWithJoins(String entityName,
      String joins, String... selectedFields) {
    return applyProjections(entityName, joins, selectedFields);
  }
  /**
   * entityName 의 selectedFields 에 대한 projection HQL (즉 select/from구문) 을 구함 
   * @param entityName 이 entity 를 select 함
   * @param joins 추가 join 들. 없으면 null
   * @param selectedFields 이 field 들을 select 함
   * @return entityName 의 selectedFields 에 대한 projection HQL (즉 select/from구문)
   */
  public static final StringBuilder applyProjections(String entityName, String joins,
      String... selectedFields) {
    StringBuilder query = new StringBuilder(64);
    if (selectedFields.length > 0) {
      query.append("SELECT ");
      for (String field : selectedFields) {
        query.append(field).append(',');
      }
      query.setLength(query.length() - 1);
      query.append(' ');
    }
    query.append("FROM ").append(entityName);
    if (joins != null) {
      query.append(" ").append(joins);
    }
    query.append(_WHERE_);
    return query;
  }
  
  /**
   * queryString 에 검색 조건들 (conditions) 들을 where 조건에 추가 함
   * @param queryString 여기에 검색 조건들을 추가 함
   * @param conditions 검색 조건 들
   * @return parameter 값들
   */
  public static final List<Object> applyConditions(StringBuilder queryString, Map<String, ?> conditions) {
    List<Object> parameters = new ArrayList<Object>();
    applyConditions(queryString, parameters, conditions);
    return parameters;
  }

  /**
   * queryString 에 검색 조건들 (conditions) 들을 where 조건에 추가 함
   * @param queryString 여기에 검색 조건들을 추가 함
   * @param parameters parameter 값들
   * @param conditions 검색 조건 들
   * @return parameter 값들
   */
  public static final void applyConditions(StringBuilder queryString, List<Object> parameters, Map<String, ?> conditions) {
    if (conditions != null) {
      for (Entry<String, ?> entry : conditions.entrySet()) {
        Object value = entry.getValue();
        String name = entry.getKey();
        applyCondition(queryString, parameters, name, value, true);
      }
    }
  }

  private static final String []toStringArray(Object v) {
    if (v instanceof String[]) {
      return (String[])v;
    } else if (v instanceof Collection<?>) {
      Collection<?> c = (Collection<?>) v;
      String []result = new String[c.size()];
      int i = 0;
      for (Object obj : c) {
        result[i++] = obj == null ? null : obj.toString();
      }
      return result;
    } else {
      return StringUtils.splitPreserveAllTokens(v.toString(), ',');
    }
  }
  /**
   * queryString 에 한 검색 조건을 where 조건에 추가 함
   * @param queryString 여기에 검색 조건들을 추가 함
   * @param parameters parameter 값들
   * @param names parameter 이름들
   * @param value parameter 값
   */
  public static final void applyCondition(Appendable queryString, Collection<Object> parameters,
      Object names, Object value) {
    applyCondition(queryString, parameters, names, value, true);
  }
  @SuppressWarnings("unchecked")
  private static final int applyCondition(Appendable queryString, Collection<Object> parameters,
      Object names, Object value, boolean appendAND) {
    if (value == null || names == null) {
      return 0;
    }
    try {
      String [] spName;
      if (value instanceof Map) {
        spName = new String[]{names.toString()};
      } else {
        spName = toStringArray(names);
      }
      if (spName.length > 1 || value instanceof Map) {
        queryString.append(LPAREN);
      }
      int addedParams = 0;
      for (String name : spName) {
        if (!"".equals(value)) {
          if (value instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) value;
            if (c.size() > 0) {
              queryString.append(name).append(" IN (");
              for (Iterator<?> iter = c.iterator(); iter.hasNext(); ) {
                Object item = iter.next();
                queryString.append("?");
                if (iter.hasNext()) {
                  queryString.append(',');
                }
                parameters.add(item);
                addedParams++;
              }
              queryString.append(')');
            }
          } else if (value instanceof Object []) {
            Object [] arr = (Object []) value;
            if (arr.length > 0) {
              queryString.append(name).append(" IN (");
              for (int i = 0, len=arr.length; i < len; i++) {
                queryString.append(i>0?",?":"?");
                parameters.add(arr[i]);
                addedParams++;
              }
              queryString.append(')');
            }
          } else if (value instanceof Map<?,?>) {
            Map<String, ?> args = (Map<String, ?>) value;
            int argumentIdx = name.indexOf(ARGUMENTS_ARG_NAME);
            if (argumentIdx != -1 || args.size()==0) {
              queryString.append(name.substring(0, argumentIdx));
              for (Entry<String, ?> entry : args.entrySet()) {
                addedParams += applyCondition(queryString, parameters, entry.getKey(), entry.getValue(), false);
              }
              queryString.append(name.substring(argumentIdx + ARGUMENTS_ARG_NAME.length()));
            } else if (name.indexOf(':') != -1) {
              Matcher m = NAMED_PARAM.matcher(name);
              int last = 0;
              while (m.find()) {
                String key = m.group(0).substring(1);
                queryString.append(name.substring(last, m.start())).append("?");
                parameters.add(args.get(key));
                last = m.end();
                addedParams++;
              }
              queryString.append(name.substring(last));
            } else {
              queryString.append(name);
            }
            addedParams++;
          } else if (value == IQueryGenerator.NULL) {
            queryString.append((name.endsWith("<>")?name.substring(0, name.length()-2):name) + " IS" + (name.endsWith("<>")?" NOT":"") + " NULL");
            addedParams++;
          } else {
            queryString.append(name);
            if (fieldOperator(name) == EQ) {
              queryString.append('=');
            }
            queryString.append(" ?");
            parameters.add(value);
            addedParams++;
          }
          queryString.append(_OR_);
        }
      }
      if (charAt(queryString, length(queryString) - 1) == '(') {
        setLength(queryString, length(queryString) - 1);
      }
      removeEnd(queryString, _OR_);
      if (addedParams > 0) {
        if (spName.length > 1 || value instanceof Map) {
          queryString.append(")");
        }
        if (appendAND) {
          queryString.append(_AND_);
        }
      }
      return addedParams;
    } catch (IOException ex) {
      // Never happens
    }
    return 0;
  }
  private static final void removeEnd(Appendable a, String match) {
    if (a instanceof StringBuilder) {
      StringBuilder b = (StringBuilder) a;
      if (b.length() >= match.length() && b.substring(b.length()-match.length()).equals(match)) {
        b.setLength(b.length() - match.length());
      }
    } else {
      StringBuffer b = (StringBuffer) a;
      if (b.length() >= match.length() && b.substring(b.length()-match.length()).equals(match)) {
        b.setLength(b.length() - match.length());
      }
    }
  }
  
  private static final int length(Appendable a) {
    return a instanceof StringBuilder ? ((StringBuilder) a).length() : ((StringBuffer) a).length();
  }
  private static final char charAt(Appendable a, int i) {
    return a instanceof StringBuilder ? ((StringBuilder) a).charAt(i) : ((StringBuffer) a).charAt(i);
  }
  private static final void setLength(Appendable a, int len) {
    if (a instanceof StringBuilder) {
      ((StringBuilder) a).setLength(len);
    } else {
      ((StringBuffer) a).setLength(len);
    }
  }
  
  
  private static final void setParameter(Query query, List<Object> parameters) {
    if (parameters != null) {
      int i = 0;
      for (Object obj : parameters) {
        query.setParameter(i, obj);
        i++;
      }
    }
  }
   

  /**
   * name 에 해당되는 oeprator key 를 구함
   * @param name
   * @return operator constant 중 하나
   */
  public static int fieldOperator(String name) {
    if (name.length() <= 1) {
      return EQ;
    }
    for (int i = 1, len=OPERATOR_STR.length; i < len; i++) {
      if (name.endsWith(OPERATOR_STR[i])) {
        return i;
      }
    }
    return EQ;
  }

  /**
   * name 에서 실제 field name 만 구함
   * @param name 여기서 field name 을 빼내는 것임
   * @return name 에서 실제 field name 만
   */
  public static String fieldName(String name) {
    int nameLen = name.length();
    if (nameLen <= 1) {
      return name;
    }
    for (int i = 1, len=OPERATOR_STR.length; i < len; i++) {
      if (name.endsWith(OPERATOR_STR[i])) {
        return name.substring(0, nameLen - OPERATOR_STR[i].length());
      }
    }
    return name;
  }
  
  public static String getTableNameByClass(Class<?> cls){
    
    Table tableInfo = cls.getAnnotation(Table.class);
    if(tableInfo == null) return null;
    
    return tableInfo.name();
    
  }
}
