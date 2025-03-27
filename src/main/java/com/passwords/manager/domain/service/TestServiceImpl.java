package com.passwords.manager.domain.service;

public class TestServiceImpl implements TestService {

	@Override
	public String testMethod(String param) {
		String result = "Hello " + param;
		System.out.println("Result: " + result);
		return result;
	}
}
