package com.passwords.manager.core.cdi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.core.cdi.annotation.Repository;

/**
 * Class used for a custom dependency injection in the application.
 * To use this dependency injection the interfaces to be injected must be annotated with
 * the annotation @Inject, that can also be used as @Inject(MyClass.class) if we want to indicate
 * one specific class to be instantiated.
 * 
 * Also, the classes that can be instantiated must be annotated with the annotation @Component, to 
 * indicate to the process that this classes must be scanned during the application startup
 */
public class DependencyInjection {

	private static final Logger logger = LogManager.getLogger(DependencyInjection.class);

	// The base package to be used must be a package that contains at least one class inside
	// It should be the package where we invoke the "main" method.
	private static final String BASE_PACKAGE = "com.passwords.manager";

	private static final Map<Class<?>, Class<?>> IMPLEMENTATIONS = new HashMap<>();

	/**
	 * Load all the classes that can be injected.
	 * This method must be executed only one time at the start of the application execution.
	 * 
	 */
	public static void load() {
		logger.debug("Start loading dependencies on the system");
		// Find all the application packages
		Set<String> packages = new HashSet<>();
		packages = findAllPackagesRecursive(BASE_PACKAGE, packages);

		// For each package find all the classes that are inside
		Set<Class<?>> classes = new HashSet<>();
		packages.forEach(subpackage -> {
			logger.debug("Package: " + subpackage);
			classes.addAll(findAllClasses(subpackage));
		});

		// For each class check if they are annotated as @Component and in that case
		// look for the interfaces that are using.
		classes.forEach(clazz -> {
			if (clazz.isAnnotationPresent(Component.class)) {
				logger.debug("Found class annotated with @Component: " + clazz);
				Class<?>[] interfaces = clazz.getInterfaces();

				if (interfaces.length > 0)
					IMPLEMENTATIONS.put(interfaces[0], clazz);
			} else if (clazz.isAnnotationPresent(Repository.class)) {
				logger.debug("Found interface annotated with @Repository: " + clazz);
				Class<?>[] interfaces = clazz.getInterfaces();

				logger.debug("TEST--Adding repo class: " + clazz + " super class: " + interfaces[0]);
				Type[] genericInterfaces = clazz.getGenericInterfaces();
				ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				for (Type actualType : actualTypeArguments) {
					if (actualType instanceof Class) {
						Class<?> entityClass = (Class<?>) actualType;
						System.out.println("TEST--Generic type: " + entityClass);
					}
				}
				// TODO Use interfaces[0] and entityClass to create a Proxy class, that is what we'll need to instanciate
				IMPLEMENTATIONS.put(clazz, interfaces[0]);
			}
		});
		logger.debug("All dependencies loaded");
	}

	/**
	 * Injects all the dependencies inside one class and inside the classes that are injected.
	 * This method must be call at the start of every class that uses Dependency Injection.
	 * To avoid call this method explicity in every class, you can extends the class com.passwords.manager.core.cdi.App
	 * that will call this method automatically.
	 * 
	 * @param object The object class where we want to inject the classes
	 * @throws RuntimeException When some error happens during the dependency injection execution.
	 */
	public static void injectDependencies(Object object) throws RuntimeException {
		// use reflection to instanciate the classes annotated with @Inject
		Class<?> clazz = object.getClass();
		logger.debug("Injecting dependencies for class: " + clazz);
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Inject.class)) {
				Class<?> injectedInterface = field.getType();
				logger.debug("Service to be injected: " + injectedInterface);

				try {
					// If the @Inject annotation has a value, use that class as the implementation class
					// Else get the implementation class loaded at application startup
					Class<?> implementationClass = field.getAnnotation(Inject.class).value();
					if (implementationClass == Void.class) {
						implementationClass = IMPLEMENTATIONS.get(injectedInterface);
					}

					Object instance = implementationClass.getDeclaredConstructor().newInstance();
					field.setAccessible(true);
					field.set(object, instance);

					injectDependencies(instance); // recursive call to inject also services inside the injected classes
				} catch (Exception e) {
					throw new RuntimeException("Failed to inject dependency " + field.getName(), e);
				}
			}
		}
		logger.debug("All dependencies injected");
	}

	/**
	 * This method find all the application packages in a recursive way.
	 * For each package the method calls to itself to get the subpackages that are inside it.
	 * 
	 * @param packagePath The package path to search
	 * @param packages The Set collection where all the previous packages found were added
	 * @return The Set collection where all the packages found are added
	 */
	private static Set<String> findAllPackagesRecursive(String packagePath, Set<String> packages) {
		BufferedReader reader = readPackage(packagePath);
		
		Iterator<String> iterator = reader.lines().iterator();
		while(iterator.hasNext()) {
			String line = iterator.next();
			if (!line.endsWith(".class")) {
				String subpackage = packagePath + "." + line;
				packages.add(subpackage);
				packages = findAllPackagesRecursive(subpackage, packages);
			}
		}
		return packages;
	}

	/**
	 * This method finds all the classes that are inside one given package
	 * 
	 * @param packagePath The package to be scan
	 * @return A set of classes found
	 */
	private static Set<Class<?>> findAllClasses(String packagePath) {
		BufferedReader reader = readPackage(packagePath);

		Set<Class<?>> classes = reader.lines()
		 								.filter(line -> line.endsWith(".class"))
		 								.map(classFile -> getClass(packagePath, classFile))
		 								.collect(Collectors.toSet());
		logger.debug("Classes: ");
		classes.forEach(c -> logger.debug("--> " + c));

		return classes;
	}

	/**
	 * Read all the content inside one java package
	 * The method will replace the dots in the package with the slash to treat the path as a directory path
	 * 
	 * @param packagePath The package path
	 * @return The BufferedReader that can be used to read the content of the directory path.
	 */
	private static BufferedReader readPackage(String packagePath) {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(packagePath.replace(".", "/"));
		return new BufferedReader(new InputStreamReader(inputStream));
	}

	/**
	 * Get the Class object from the package path and the class name
	 * 
	 * @param packagePath The string with the full package path
	 * @param classFile The Class name string
	 * @return The Class object
	 */
	private static Class<?> getClass(String packagePath, String classFile) {
		try {
			return Class.forName(packagePath + "." + classFile.substring(0, classFile.lastIndexOf(".")));
		} catch (ClassNotFoundException e) {
			logger.debug("Error. Class " + classFile + " Not Found.");
			e.printStackTrace();
			return null;
		}
		
	}
}
