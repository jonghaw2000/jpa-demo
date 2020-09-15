package com.exam.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exam.common.util.DAOUtil;

@Repository
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class GeneratedDAO implements IGeneratedDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
   * {@inheritDoc}
   */
  @Transactional(readOnly=true)
  public <Y> List<Y> selectAll(Class<?> entityClass, String orderBy, String cacheRegion, String... selectedFields) {
    return selectBy2(entityClass, null, null, null, null, orderBy, cacheRegion, selectedFields);
  }
  
  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly=true)
  public <Y> List<Y> selectBy2(Class<?> entityClass, String fieldName, Object value, String fieldName2,
      Object value2, String orderBy, String cacheRegion, String ... selectedFields) {
    StringBuilder buf = DAOUtil.createQueryString(entityClass, selectedFields);
    List<Object> params = new ArrayList<>();
    DAOUtil.applyCondition(buf, params, fieldName, value);
    DAOUtil.applyCondition(buf, params, fieldName2, value2);
    Query q = DAOUtil.createQuery(entityManager, buf, 0, 0, orderBy, cacheRegion, params);
    return q.getResultList();
  }
  

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly=true)
  public <Y,C> List<Y> selectBy(Class<?> entityClass, String joins, 
      Map<String, C> conditions,
      int start, int limit, String orderBy, String groupBy, String cacheRegion, String ... selectedFields) {
    String entityName = findEntityName(entityClass, conditions);
    return selectBy(entityName, joins, conditions, start, limit, orderBy, groupBy,
        cacheRegion, selectedFields);
  }

  /**
   * {@inheritDoc}
   */
  public <Y,C> List<Y> selectBy(String entityName, String joins, Map<String, C> conditions,
      int start, int limit, String orderBy, String groupBy, String cacheRegion,
      String... selectedFields) {
    StringBuilder buf = DAOUtil.createQueryStringWithJoins(entityName, joins, selectedFields);
    List<Object> params = DAOUtil.applyConditions(buf, removeClassEntry(conditions));
    Query q = DAOUtil.createQuery(entityManager, buf, start, limit, orderBy, groupBy, cacheRegion, params);
    return q.getResultList();
  }
  
  private static final <C> String findEntityName(Class<?> entityClass,
      Map<String, C> conditions) {
    String entityName = null;
    if (conditions != null) {
      Object cls = conditions.get("class");
      entityName = objToEntityName(cls);
    }
    if (entityName == null) {
      entityName = entityClass.getSimpleName();
    }
    return entityName;
  }

  private static final String objToEntityName(Object cls) {
    if (cls != null) {
      if (cls instanceof Class<?>) {
        return ((Class<?>) cls).getSimpleName();
      }
      return cls.toString();
    }
    return null;
  }
  
  private static final <C> Map<String, C> removeClassEntry(Map<String, C> conditions) {
    if (conditions == null) {
      return conditions;
    }
    if (conditions instanceof HashMap<?,?>) {
      conditions.remove("class");
    } else if (conditions.containsKey("class")) {
      Map<String, C> c = new HashMap<String, C>(conditions.size() * 4 / 3 + 1);
      for (Entry<String, C> entry : conditions.entrySet()) {
        if (!"class".equals(entry.getKey())) {
          c.put(entry.getKey(), entry.getValue());
        }
      }
      return c;
    }
    return conditions;
  }
  
}
