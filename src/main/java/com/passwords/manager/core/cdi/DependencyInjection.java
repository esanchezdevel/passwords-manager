package com.passwords.manager.core.cdi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.passwords.manager.core.cdi.annotation.Inject;
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
				Class<?> injectedInterface = field.getType();
				System.out.println("Service to be injected: " + injectedInterface);
				try {
					Class<?> implementationClass = findImplementationClass(injectedInterface);

					Object instance = implementationClass.getDeclaredConstructor().newInstance();
					field.setAccessible(true);
					field.set(object, instance);
				} catch (Exception e) {
					throw new RuntimeException("Failed to inject dependency " + field.getName(), e);
				}
			}
		}
	}


	/**
	 * This method receive one java interface, and returns one class that implements that interface
	 * 
	 * @param injectedInterface The interface to check
	 * @return A class that implements the interface
	 */
	private static Class<?> findImplementationClass(Class<?> injectedInterface) {
		// Find all the application package
		System.out.println("SubPackages:");
		Set<String> packages = new HashSet<>();
		packages = findAllPackagesRecursive(BASE_PACKAGE, packages);
		
		// For each package find all the classes that are inside and check if it implements the interface
		packages.forEach(subpackage -> System.out.println("--> " + subpackage));

		// Set<Class<?>> classes = reader.lines()
		// 								.filter(line -> line.endsWith(".class"))
		// 								.map(classFile -> getClass(classFile))
		// 								.collect(Collectors.toSet());
		// System.out.println("Classes: ");
		// classes.forEach(c -> System.out.println("--> " + c));

		return interfaceToImplementation.getOrDefault(injectedInterface, injectedInterface); // Temporal solution to be changed
	}

	/**
	 * This method find all the application packages in a recursive way.
	 * For each package the method calls to itself to get the subpackages that are inside it.
	 * 
	 * @param basePackage The package path to search
	 * @param packages The Set collection where all the previous packages found were added
	 * @return The Set collection where all the packages found are added
	 */
	private static Set<String> findAllPackagesRecursive(String basePackage, Set<String> packages) {
		BufferedReader reader = readPackage(basePackage);
		
		Iterator<String> iterator = reader.lines().iterator();
		while(iterator.hasNext()) {
			String line = iterator.next();
			if (!line.endsWith(".class")) {
				String subpackage = basePackage + "." + line;
				packages.add(subpackage);
				packages = findAllPackagesRecursive(subpackage, packages);
			}
		}
		return packages;
	}

	private static BufferedReader readPackage(String packageName) {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"));
		return new BufferedReader(new InputStreamReader(inputStream));
	}

	private static Class<?> getClass(String classFile) {
		try {
			return Class.forName(BASE_PACKAGE + "." + classFile.substring(0, classFile.lastIndexOf(".")));
		} catch (ClassNotFoundException e) {
			System.out.println("Error. Class " + classFile + " Not Found.");
			e.printStackTrace();
			return null;
		}
		
	}
}
