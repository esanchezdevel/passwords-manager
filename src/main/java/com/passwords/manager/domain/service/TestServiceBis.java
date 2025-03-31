package com.passwords.manager.domain.service;

public class TestServiceBis implements TestService {

	@Override
	public String testMethod(String param) {
		String result = "Eeeeeooooo!!!! " + param;
		System.out.println(result);
		return result;
	}
}
