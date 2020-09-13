package com.exam.shop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ShopMain {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("global", "winter");
		context.scan("com.exam.shop");
		context.refresh();
	}
}
