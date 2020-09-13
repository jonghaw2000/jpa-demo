package com.exam.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.exam.shop.Battery;
import com.exam.shop.Disc;
import com.exam.shop.Product;

@Configuration
@Profile({"summer","winter"})
public class ShopConfigurationSumWin {
	
	@Bean
	public Product aaa() {
		Battery p1 = new Battery();
		p1.setName("AAA");
		p1.setPrice(2.0);
		p1.setRechargeable(true);
		return p1;
	}
	
	@Bean
	public Product cdrw() {
		Disc p2 = new Disc("CD-RW", 1.0, 0);
		p2.setCapacity(700);
		return p2;
	}
	
	@Bean
	public Product dvdrw() {
		Disc p2 = new Disc("DVD-RW", 2.5, 0);
		p2.setCapacity(700);
		return p2;
	}
}
