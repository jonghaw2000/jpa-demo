package com.exam.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.exam.config.JpaConfig;
import com.exam.entity.Singer;

public class SingerServiceTest {
	private static final Logger log = LoggerFactory.getLogger(SingerServiceTest.class);

	private GenericApplicationContext ctx;
	private SingerService singerService;
	
	@Before
	public void setUp() {
		ctx = new AnnotationConfigApplicationContext(JpaConfig.class);
		singerService = ctx.getBean(SingerService.class);
		assertNotNull(singerService);
	}
	
	@Test
	public void testFindAll() {
		List<Singer> singers = singerService.findAll();
		assertEquals(3, singers.size());
		listSingers(singers);
	}
	
	@Test
	public void testFindAllWithAlbum() {
		List<Singer> singers = singerService.findAllWithAlbum();
		assertEquals(3, singers.size());
		listSingersWithAlbum(singers);
	}
	
	private static void listSingersWithAlbum(List<Singer> singers) {
		log.info("------ 가수가 다룰 수 있는 악기 목록이 포함된 가수 목록:");
		singers.forEach( s -> {
			log.info(s.toString());
			if(s.getAlbums() != null) {
				s.getAlbums().forEach( a -> log.info("\t" + a.toString()));
			}
			if(s.getInstruments() != null) {
				s.getInstruments().forEach( i -> 
					log.info("\tInstrument: " + i.getInstrumentId()));
			}
		});
	}
	
	private static void listSingers(List<Singer> singers) {
		log.info("----------------가수 목록");
		singers.forEach( s -> log.info(s.toString()) );
	}
	
	@After
	public void tearDown() {
		ctx.close();
	}
}
