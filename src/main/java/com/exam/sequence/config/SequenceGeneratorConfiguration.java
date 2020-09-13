package com.exam.sequence.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.exam.sequence.SequenceGenerator;

@Configurable
@ComponentScan(
		includeFilters = {
				@ComponentScan.Filter(
					type=FilterType.REGEX,
					pattern = {"com.exam.sequence.*Dao",
							"com.exam.sequence.*Service"})
		},
		excludeFilters = {
				@ComponentScan.Filter(
						type=FilterType.ANNOTATION,
						classes = {org.springframework.stereotype.Controller.class})
		}
)
public class SequenceGeneratorConfiguration {
	
//	@Bean
//	public SequenceGenerator sequenceGenerator() {
//		SequenceGenerator sequen = new SequenceGenerator();
//		sequen.setPrefix("30");
//		sequen.setSuffix("A");
//		sequen.setInitial(10000);
//		return sequen;
//	}
}
