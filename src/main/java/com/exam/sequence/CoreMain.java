package com.exam.sequence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.exam.sequence.config.SequenceGeneratorConfiguration;

public class CoreMain {

	public static void main(String[] args) {
		ApplicationContext context =
				new AnnotationConfigApplicationContext("com.exam.sequence");

		SequenceDao sequenceDao = context.getBean(SequenceDao.class);
		
		System.out.println(sequenceDao.getNextValue("IT"));
		System.out.println(sequenceDao.getNextValue("IT"));
	}

}
