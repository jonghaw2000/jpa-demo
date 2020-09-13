package com.exam.calculator;

import org.springframework.stereotype.Component;

@Component("arithmeticCalculator")
public class ArithmeticCalculatorImpl implements ArithmeticCalculator {

	@Override
	public double add(double a, double b) {
		double result = a + b;
		System.out.println(a + " + " + b + " = " + result);
		return result;
	}

	@Override
	public double sub(double a, double b) {
		double result = a - b;
		System.out.println(a + " - " + b + " = " + result);
		return result;
	}

	@Override
	public double mul(double a, double b) {
		double result = a * b;
		System.out.println(a + " * " + b + " = " + result);
		return result;
	}

	@Override
	public double div(double a, double b) {
		double result = a / b;
		System.out.println(a + " / " + b + " = " + result);
		return result;
	}

}
