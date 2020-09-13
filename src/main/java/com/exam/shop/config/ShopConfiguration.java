package com.exam.shop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.exam.shop.Battery;
import com.exam.shop.Cashier;
import com.exam.shop.Disc;
import com.exam.shop.DiscountFactoryBean;
import com.exam.shop.Product;
import com.exam.shop.ProductCreator;

@Configuration
@ComponentScan("com.exam.shop")
public class ShopConfiguration {
	
	@Bean
    public Product aaa() {
        Battery aaa = new Battery("AAA", 2.5, 0);
		return aaa;
    }

    @Bean
    public Product cdrw() {
    	Disc aaa = new Disc("CD-RW", 1.5, 0);
        return aaa;
    }

    @Bean
    public Product dvdrw() {
    	Disc aaa = new Disc("DVD-RW", 3.0, 0);
        return aaa;
    }

    @Bean
    public DiscountFactoryBean discountFactoryBeanAAA() {
    	DiscountFactoryBean factory = new DiscountFactoryBean();
    	factory.setProduct(aaa());
    	factory.setDiscount(0.2);
    	return factory;
    }
    
    @Bean
    public DiscountFactoryBean discountFactoryBeanCDRW() {
    	DiscountFactoryBean factory = new DiscountFactoryBean();
    	factory.setProduct(cdrw());
    	factory.setDiscount(0.1);
    	return factory;
    }
    
    @Bean
    public DiscountFactoryBean discountFactoryBeanDVDRW() {
    	DiscountFactoryBean factory = new DiscountFactoryBean();
    	factory.setProduct(dvdrw());
    	factory.setDiscount(0.1);
    	return factory;
    }
}
