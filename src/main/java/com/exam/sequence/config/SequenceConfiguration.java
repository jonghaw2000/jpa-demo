package com.exam.sequence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.exam.sequence.DatePrefixGenerator;
import com.exam.sequence.SequenceGenerator;

@Configuration
public class SequenceConfiguration {
	
	@Bean
	@DependsOn("datePrefixGenerator")
	public SequenceGenerator sequenceGenerator() {
		SequenceGenerator sequence = new SequenceGenerator();
		sequence.setInitial(10000);
		sequence.setSuffix("A");
		return sequence;
	}
	
	@Bean
	public DatePrefixGenerator datePrefixGenerator() {
		DatePrefixGenerator dpg = new DatePrefixGenerator();
		dpg.setPattern("yyyyMMdd");
		return dpg;
	}
	
//	@Bean
//	public SequenceGenerator sequenceGenerator() {
//		SequenceGenerator sequence = new SequenceGenerator();
//		sequence.setInitial(10000);
//		sequence.setSuffix("A");
//		sequence.setPrefixGenerator(datePrefixGenerator());
//		return sequence;
//	}
}
