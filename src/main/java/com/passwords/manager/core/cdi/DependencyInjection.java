package com.passwords.manager.core.cdi;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

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
	private static final Map<Class<?>, RepositoryData> REPOSITORIES = new HashMap<>();

	/**
	 * Load all the classes that can be injected.
	 * This method must be executed only one time at the start of the application execution.
	 * 
	 */
	public static void load() {
		logger.debug("Start loading dependencies on the system");

		Reflections reflections = new Reflections(BASE_PACKAGE);

		// Find all classes annotated with @Component
		Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
		for (Class<?> clazz : components) {
			logger.debug("Found class annotated with @Component: " + clazz);
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length > 0) {
				IMPLEMENTATIONS.put(interfaces[0], clazz);
			}
		}

		// Find all interfaces annotated with @Repository
		Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
		for (Class<?> clazz : repositories) {
			logger.debug("Found interface annotated with @Repository: " + clazz);
			Class<?>[] interfaces = clazz.getInterfaces();

			logger.debug("Adding repo class: " + clazz + " super class: " + interfaces[0]);
			Type[] genericInterfaces = clazz.getGenericInterfaces();
			ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

			Class<?> entityClass = null;
			for (Type actualType : actualTypeArguments) {
				if (actualType instanceof Class<?>) {
					entityClass = (Class<?>) actualType;
					logger.debug("Generic type: " + entityClass);
				}
			}
			REPOSITORIES.put(clazz, new RepositoryData(interfaces[0], entityClass));
		}

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
					Object instance = null;
					if (implementationClass == Void.class) {
						if (IMPLEMENTATIONS.containsKey(injectedInterface)) {
							implementationClass = IMPLEMENTATIONS.get(injectedInterface);

							instance = implementationClass.getDeclaredConstructor().newInstance();
						} else if (REPOSITORIES.containsKey(injectedInterface)) {
							RepositoryData repositoryData = REPOSITORIES.get(injectedInterface);
							
							logger.debug("repository data for {}: {}", injectedInterface, repositoryData);
							instance = createProxy(injectedInterface, repositoryData.entity());
						} else {
							logger.error("Implementation not found");
							throw new RuntimeErrorException(new Error("Implementation not found for interface " + injectedInterface));
						}
					} else {
						instance = implementationClass.getDeclaredConstructor().newInstance();
					}
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
	 * Create a Proxy class that implements the interface received as a paramter, and use the entityClass as the Generic class
	 * used by the interface.
	 * The Proxy at the end will be the equivalent to something like this:
	 * Object proxyClass = new TestInterface<Entity.class>();
	 * 
	 * @param interfaceClass The Interface to be implemented by the Proxy
	 * @param entityClass The Class to be used as the Generic of the interface
	 * @return The Object of the Proxy class created
	 */
	public static Object createProxy(Class<?> interfaceClass, Class<?> entityClass) {
		return Proxy.newProxyInstance(
				interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass },
				new GenericInvocationHandler(entityClass));
	}

	/**
	 * Nested class used to generate the Proxy implementation classes.
	 * In this class we can find some important points needed to make the Proxy classes
	 * works properly.
	 */
	private static class GenericInvocationHandler implements InvocationHandler {
		private final Class<?> entityClass;

		public GenericInvocationHandler(Class<?> entityClass) {
			this.entityClass = entityClass;
		}

		/**
		 * In this method we pass the Proxy class, the method to be invoked and the arguments passed to that method
		 * 
		 * @param proxy The Proxy object
		 * @param method The method to be invoked
		 * @param args The arguments to be passed to the method
		 * @return The invokation
		 * @throws Throwable
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			logger.debug("Method called: " + method.getName());

			if (method.isDefault()) {
				logger.debug("Is a default method");
				return invokeDefaultMethod(proxy, method, args);
			}
	
			// Custom logic for other methods
			if (method.getReturnType().isAssignableFrom(entityClass)) {
				logger.debug("Custom logic");
				return entityClass.getDeclaredConstructor().newInstance();
			}
	
			if (method.getName().equals("getEntityClass")) {
				return entityClass;
			}

			return null;
		}

		/**
		 * Invoke a default method implemented in one interface.
		 * As by default the Proxy don't call to the default methods, we have to 
		 * invoke them in this special way to make them work
		 * 
		 * @param proxy The Proxy object
		 * @param method The method to be invoked
		 * @param args The arguments to be passed to the method
		 * @return The invokation
		 * @throws Throwable
		 */
		private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
			Class<?> declaringClass = method.getDeclaringClass();
			
			MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(declaringClass, MethodHandles.lookup());
		
			return lookup.findSpecial(
					declaringClass,
					method.getName(),
					MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
					declaringClass)
				.bindTo(proxy)
				.invokeWithArguments(args != null ? args : new Object[0]);
		}
	}
}
