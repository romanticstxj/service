package com.madhouse.platform.premiummad.reporttask;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTask {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("/spring.xml");
	    
	}
}
