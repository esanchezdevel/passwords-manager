package com.passwords.manager.core.cdi;

public class App {

	// This block of code is executed before the constructor finished, but after the object "this" is allocated
	{
		DependencyInjection.injectDependencies(this);
	}
}
