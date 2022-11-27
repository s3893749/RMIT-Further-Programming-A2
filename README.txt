RMIT / OUA
COSC2288 / COSC2684/ CPT222
Assignemnt 2 MyHealth JavaFX GUI Application
Further Programming A2 Submission
Jack Harris | s3893749@student.rmit.edu.au

|---------------------------------------------------------------------------|
|                            HOW TO COMPILE                                 |
|---------------------------------------------------------------------------|
The project has been created using IntelliJ and Maven, to compile the program
for testing you can simply click the green arrow at the top left hand side of
the program.

Alternatively if you wish to compile the program to be used standalone or by
others then you need to click file -> project structure -> artifacts, from
here you can click plus to add a new artifact -> jar -> from module with
dependencies. As this is a maven project I would recommend for simplicity that
the program be compiled via a IntelliJ artifact instead of a standard Java
commandline compile as was done in A1.

Once you have the artifact added then simply press the build button on the top
ribbon menu, then select artifact, select the one you created then build.


|---------------------------------------------------------------------------|
|                         HOW TO RUN UNIT TESTS                             |
|---------------------------------------------------------------------------|
To make the unit tests easy to run I have placed them all inside the testing
package "com.jackgharris.cosc2288.a2.tests" to run all the testing classes in
that package simply right-click on the package via the project view on the left
and select run "Run tests in com.jackgharris.cosc2288.a2.tests". IntelliJ will
then go ahead and run all the class files and the tests compiled with in.

From there you can export the results as a .html file and place them in the
dedicated "unit testing results" folder.


|---------------------------------------------------------------------------|
|                         JDK Version & Compiler                            |
|---------------------------------------------------------------------------|
For this project I have used the follow:

- open JDK version 19 & Compiler (installed via intelliJ)
- openJavaFX version 17.0.2 (installed via intelliJ with Maven)
- sqlite-jdbc by org.xerial version 3.39.3.0 (installed via intelliJ with Maven)
- commons-validator version 1.7 (installed via intelliJ with Maven)
- Junit version 4.13.2 (installed via intelliJ with Maven)
- hamcrest-core version 1.3 (installed via intelliJ with Maven)
