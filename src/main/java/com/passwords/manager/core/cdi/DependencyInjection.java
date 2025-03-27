package com.passwords.manager.core.cdi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.passwords.manager.controller.WelcomeViewController;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.service.TestService;
import com.passwords.manager.domain.service.TestServiceImpl;
import com.sun.tools.javac.Main;

public class DependencyInjection {

	private static final String BASE_PACKAGE = "com.passwords.manager";

	private static final List<Class<?>> classes = List.of(Main.class, TestServiceImpl.class);

	private static final Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();

    static {
        // Register interfaces and their implementations
        //interfaceToImplementation.put(TestService.class, TestServiceImpl.class);
		scanClasses();
    }

	private static void scanClasses() {
		
		// TODO scan all classes from the BASE_PACKAGE
		classes.forEach(clazz -> {

			// for each class get the interfaces
			Class<?>[] interfaces = clazz.getInterfaces(); // TODO remove when we have the real classes in a for loop

			if (interfaces.length > 0) {
				System.out.println("Interface found for class " + clazz + ": " + interfaces[0]);
				interfaceToImplementation.put(interfaces[0], clazz);
			}
		});
	}

	public static void injectDependencies(Object object) {
		// use reflection to instanciate the classes annotated with @Inject
		Class<?> clazz = object.getClass();
		System.out.println("Injecting dependency for class: " + clazz);
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Inject.class)) {
				Class<?> fieldType = field.getType();
				System.out.println("Service to be injected: " + fieldType);
				try {
					Class<?> implementationClass = interfaceToImplementation.getOrDefault(fieldType, fieldType);

					Object instance = implementationClass.getDeclaredConstructor().newInstance();
					field.setAccessible(true);
					field.set(object, instance);
				} catch (Exception e) {
					throw new RuntimeException("Failed to inject dependency " + field.getName(), e);
				}
			}
		}
	}
}
