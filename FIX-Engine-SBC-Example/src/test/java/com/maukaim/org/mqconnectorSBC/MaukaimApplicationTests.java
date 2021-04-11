package com.maukaim.org.mqconnectorSBC;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MaukaimApplicationTests {

	@Autowired
	private BeanFactory beanFactory;


	@Test
	void contextLoads() {
		List<String> packages = AutoConfigurationPackages.get(beanFactory);
		System.out.println(packages);

	}

}
