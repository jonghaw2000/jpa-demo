package com.exam.shop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Cashier implements BeanNameAware{
	
	private String fileName;
	
	@Override
	public void setBeanName(String beanName) {
		this.fileName = beanName;
	}

	@Value("c:/windows/Temp/cashier")
	private String path;
	
	private BufferedWriter writer;
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	@PostConstruct
	public void openFile() throws IOException{
		File targetDir = new File(path);
		if(!targetDir.exists()) {
			targetDir.mkdir();
		}
		
		File checkoutFile = new File(path, fileName + ".txt");
		if(!checkoutFile.exists()) {
			checkoutFile.createNewFile();
		}
		
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(checkoutFile, true)));
	}
	
	public void checkout(ShoppingCart cart) throws IOException{
		writer.write(new Date() + "\t" + cart.getItems() + "\r\n");
		writer.flush();
	}
	
	@PreDestroy
	public void closeFile() throws IOException{
		writer.close();
	}
}