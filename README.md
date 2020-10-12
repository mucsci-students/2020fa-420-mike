# Team mike's UML Editor

Team mike's UML Editor is an application that allows the user to add, delete, and edit UML
classes. Each UML class can hold different fields and methods, which can be added, deleted,
and renamed. Relationships between classes may be created and deleted. The user can also 
save files and load other ones to easily save progress and continue to work at a 
later time. The application runs using a Command Line Interface or a Graphical User Interface.

## Requirements:

[Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

[Gradle](https://gradle.org/install/)

## Running the application:

1. Navigate to the directory with the files and have a command prompt ready
2. Type these commands in the command prompt:
   ```gradle build```
   ```gradle shadowJar```
   ```java -jar ./build/libs/mike-all.jar (launches the GUI) or java -jar ./build/libs/mike-all.jar cli (launches the CLI)```

Gradle build builds the application, gradle shadowJar creates a jar file with all of the dependencies
installed, and java -jar ./build/libs/mike-all.jar (cli) runs the application.

### Developers:
    - Stefan Gligorevic
    - Conor Sosh
    - Phil Androwick
    - John Hynes
    - Ian Reger

**DISCLAIMER**: 
This is not a finished product. This is the state of the application after Sprint 2.
