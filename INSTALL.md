=================================================================== INSTALL ====

To build and install the fx-guice library you'll need the following tools and
dependencies:

* Java Development Kit (Version 7.0 / 1.7.0 or higher)
* JavaFX Runtime (Version 2.x), might be bundled with your JDK
* Maven (Version 3.0.3 and 3.0.4 have both been tested)

You'll probable want to activate the Maven Profile "javafx" to make sure that
the JavaFX runtime can be found in the classpath during the build process.
Make sure to set the variable "javafx-runtime-jar" to the ABSOLUTE PATH of your
jfxrt.jar file as well.

Usually all you need to do to build and install fx-guice into your local .m2
repository should be the execution of the following maven command line:

  mvn \
    -Djavafx-runtime-jar="C:\Program Files\Java\jdk1.7.0_10\jre\lib\jfxrt.jar \
    -Pjavafx \
    install
