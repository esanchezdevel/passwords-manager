# Passwords Manager Desktop application
Manage all the credentials in one place in a safe mode.<br>
Be careful, because as this application doesn't have external connections, it doesn't have a recovery system.

### How to Package the application in a Executable file
1. Download Liberica "JRE full" from official page if you don't have it: `https://bell-sw.com/pages/downloads/#jdk-21-lts`
2. Compile the project:
`$ mvn clean package`
3. Execute the JPACKAGE command to create the executable file:<br>
`jpackage --name PasswordsManagerExecutable --input target --main-jar passwords-manager-1.0-SNAPSHOT-jar-with-dependencies.jar --main-class com.passwords.manager.Main --type app-image --runtime-image <Path-To-Liberica-JRE>`
