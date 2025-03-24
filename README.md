# Passwords Manager Desktop application
Manage all the credentials in one place in a safe mode.

Be careful, because as this application doesn't have external connections, it doesn't have a recovery system.


How to Package the application in a Executable file
jpackage --name PasswordsManagerExecutable --input target --main-jar passwords-manager-1.0-SNAPSHOT.jar --main-class com.passwords.manager.Main --type app-image --runtime-image <jdk-path>