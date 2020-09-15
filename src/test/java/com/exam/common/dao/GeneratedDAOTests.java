package com.exam.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.exam.entity.Singer;

@SpringBootTest
public class GeneratedDAOTests {
  
  private static final Logger log = LoggerFactory
      .getLogger(GeneratedDAOTests.class);

  @Autowired IGeneratedDAO generatedDAO;
  
  @Test
  public void testSelectAll() {
    List<Singer> singers = generatedDAO.selectAll(Singer.class, null, null);
    singers.forEach( s -> log.info(s.toString()) );
  }
  
  @Test
  public void testselectBy() {
    Map<String, Object> conditions = new HashMap<>();
    conditions.put("id", 1);
    List<Singer> singers = 
        generatedDAO.selectBy(Singer.class, null, conditions, 0, 0, null, null, null);
    singers.forEach( s -> log.info(s.toString()) );
  }
}
