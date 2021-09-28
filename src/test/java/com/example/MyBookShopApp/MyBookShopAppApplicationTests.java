package com.example.MyBookShopApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MyBookShopAppApplicationTests {

	private final MyBookShopAppApplication application;

	@Autowired
	public MyBookShopAppApplicationTests(MyBookShopAppApplication application) {
		this.application = application;
	}

	@Test
	void contextLoads() {
		assertNotNull(application);
	}

}
