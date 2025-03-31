package com.passwords.manager.domain.service;

import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.core.cdi.annotation.Inject;

@Component
public class TestServiceImpl implements TestService {

	@Inject
	private TestService2 testService2;

	@Override
	public String testMethod(String param) {
		String result = "Hello " + param;
		System.out.println("Result: " + result);

		testService2.testMethod("Helloooo!!!!");

		return result;
	}
}
