###### Team mike's UML Editor

Team mike's UML Editor is an application that allows the user to add, delete, and edit UML
classes. Each UML class can hold different fields and methods, which can be added, deleted,
and renamed. The user can also save files and load other ones to easily save progress and 
continue to work on it at a later time. The application currently runs only using a Command
Line Interface.

## Requirements:

- Java 11
- Gradle

## Running the application:

1. Navigate to the directory with the files and have a command prompt ready
2. Type these commands in the command prompt:
- gradle build
- gradle shadowJar
- java -jar ./build/libs/mike-all.jar

Gradle build builds the application, gradle shadowJar creates a jar file with all of the dependencies
installed, and java -jar ./build/libs/mike-all.jar runs the application.

# Developers:

Stefan Gligorevic
Conor Sosh
Phil Androwick
John Hynes
Ian Reger

**DISCLAIMER**: 
This is not a finished product. This is the state of the application after Sprint 1.
