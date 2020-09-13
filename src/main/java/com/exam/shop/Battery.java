package com.exam.shop;

public class Battery extends Product {
	
	private boolean rechargeable;
	
	public Battery() {
		super();
	}

	public Battery(String name, double price, double discount) {
		super(name, price, discount);
	}

	public boolean isRechargeable() {
		return rechargeable;
	}

	public void setRechargeable(boolean rechargeable) {
		this.rechargeable = rechargeable;
	}
}
