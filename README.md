# Team mike's UML Editor

![CI testing with Gradle](https://github.com/mucsci-students/2020fa-420-mike/workflows/CI%20testing%20with%20Gradle/badge.svg?branch=develop)

[![codecov](https://codecov.io/gh/mucsci-students/2020fa-420-mike/branch/develop/graph/badge.svg)](https://codecov.io/gh/mucsci-students/2020fa-420-mike)

Team mike's UML Editor is an application that allows the user to add, delete, and edit UML
classes. Each UML class can hold different fields, methods, and parameters, which can be added, deleted,
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

   ```java -jar ./build/libs/mike-all.jar (for GUI) or java -jar ./build/libs/mike-all.jar cli (for CLI)```

### Developers:
    - Stefan Gligorevic
    - Conor Sosh
    - Phil Androwick
    - John Hynes
    - Ian Reger

### Version:

4.0.0
