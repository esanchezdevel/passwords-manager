package com.passwords.manager;

/**
 * Launcher class needed to compile with Maven the application
 * and add in the fat jar generated the JAVAFX runtime components.
 * 
 * If we don't have this Launcher class and just starts the application 
 * from the Main class, then the application works when we execute it from the
 * IDE, but it not works when we compile it with maven and run the .jar file generated.
 */
public class Launcher {

	public static void main(String[] args) {
		Main.main(args);
	}
}
