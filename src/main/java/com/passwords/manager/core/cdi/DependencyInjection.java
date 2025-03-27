package com.passwords.manager.core.cdi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.passwords.manager.core.cdi.annotation.Inject;

public class DependencyInjection {

	private static final String BASE_PACKAGE = "com.passwords.manager";

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
		// Find all the application packages
		System.out.println("SubPackages:");
		Set<String> packages = new HashSet<>();
		packages = findAllPackagesRecursive(BASE_PACKAGE, packages);
		
		// For each package find all the classes that are inside
		Set<Class<?>> classes = new HashSet<>();
		packages.forEach(subpackage -> {
			System.out.println("--> " + subpackage);
			classes.addAll(findAllClasses(subpackage));
		});

		// For each class check if it implements the interface using reflection
		System.out.println("All Classes: ");
		for (Class<?> clazz : classes) {
			System.out.println("--> " + clazz);
			Optional<Class<?>> result = Arrays.stream(clazz.getInterfaces())
													.filter(i -> i == injectedInterface)
													.findFirst();
			if (result.isPresent()) {
				System.out.println("Found implementation class: " + clazz);
				return clazz;
			}	
		}
		System.out.println("Implementation class not found. Use the same class");
		return injectedInterface;
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

	/**
	 * This method finds all the classes that are inside one given package
	 * 
	 * @param basePackage The package to be scan
	 * @return A set of classes found
	 */
	private static Set<Class<?>> findAllClasses(String basePackage) {
		BufferedReader reader = readPackage(basePackage);

		Set<Class<?>> classes = reader.lines()
		 								.filter(line -> line.endsWith(".class"))
		 								.map(classFile -> getClass(basePackage, classFile))
		 								.collect(Collectors.toSet());
		System.out.println("Classes: ");
		classes.forEach(c -> System.out.println("--> " + c));

		return classes;
	}

	private static BufferedReader readPackage(String packageName) {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"));
		return new BufferedReader(new InputStreamReader(inputStream));
	}

	private static Class<?> getClass(String basePackage, String classFile) {
		try {
			return Class.forName(basePackage + "." + classFile.substring(0, classFile.lastIndexOf(".")));
		} catch (ClassNotFoundException e) {
			System.out.println("Error. Class " + classFile + " Not Found.");
			e.printStackTrace();
			return null;
		}
		
	}
}
