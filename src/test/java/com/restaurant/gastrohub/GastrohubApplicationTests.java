package com.restaurant.gastrohub;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

class GastrohubApplicationTests {

	@Test
	void testMain(){
		String[] args = {"--spring.profiles.active=test", "--spring.main.web-application-type=none"};

		try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
			assertDoesNotThrow(() -> GastrohubApplication.main(args));
			mocked.verify(() -> SpringApplication.run(GastrohubApplication.class, args));
		}
	}
}
