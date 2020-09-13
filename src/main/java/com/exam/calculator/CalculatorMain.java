package com.exam.calculator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.exam.calculator.config.CalculatorConfiguration;

public class CalculatorMain {
	public static void main(String[] args) {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(CalculatorConfiguration.class);
		
		ArithmeticCalculator arithmeticCalculator =
				context.getBean(ArithmeticCalculator.class);
		arithmeticCalculator.add(1, 2);
		arithmeticCalculator.sub(4, 3);
		arithmeticCalculator.mul(2, 3);
		arithmeticCalculator.div(4, 2);
		
		UnitCalculator unitCalculator = 
				context.getBean(UnitCalculator.class);
		unitCalculator.kilogramToPound(10);
		unitCalculator.kilometerToMile(5);
	}
}
