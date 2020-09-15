package com.exam.common.dao;

import java.util.List;
import java.util.Map;

public interface IGeneratedDAO {
  
  <Y> List<Y> selectAll(Class<?> entityClass, String orderBy, String cacheRegion, String... selectedFields);
  
  <Y> List<Y> selectBy2(Class<?> entityClass, String fieldName, Object value, String fieldName2,
      Object value2, String orderBy, String cacheRegion, String ... selectedFields);
  

  <Y,C> List<Y> selectBy(Class<?> entityClass, String joins, 
      Map<String, C> conditions,
      int start, int limit, String orderBy, String groupBy, String cacheRegion, String ... selectedFields);
}
