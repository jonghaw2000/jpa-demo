package com.exam.common;

/**
 * 현재는 NULL constant 만 사용되는데, 추후에는 이 constant 를 IDAO 로 이동하고, 이 class 는 삭제 대상임.
 * @author uchung
 *
 */
public interface IQueryGenerator {
  /**
   * 검색 조건을 제공시 (IDAO) value 에 이 것을 넣으면 IS NULL 로 query 에 들어가게 됨.
   */
  Object NULL = new Object();

}