package com.chensoul.test;

import com.chensoul.lss.LssApp5;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


@SpringBootTest(classes = LssApp5.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class Lss5IntegrationTest {
	@Test
	public void whenLoadApplication_thenSuccess() {

	}
}
