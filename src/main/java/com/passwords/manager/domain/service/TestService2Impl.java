package com.passwords.manager.domain.service;

import com.passwords.manager.core.cdi.annotation.Component;

@Component
public class TestService2Impl implements TestService2 {

	@Override
	public void testMethod(String text) {
		System.out.println("The text: " + text);
	}
}
