To build and install the fx-guice library you'll need the following tools and
dependencies:

* Java Development Kit (Version 7.0 / 1.7.0 or higher)
* JavaFX Runtime (Version 2.x), might be bundled with your JDK
* Maven (Version 3.0.3 and 3.0.4 have both been tested)

Usually all you need to do to build and install fx-guice into your local .m2
repository should be the execution of the following maven command line:

```sh
mvn install
```

If your JavaFX runtime (jfxrt.jar) cannot be found automatically, you'll
have to set the variable `javafx-runtime-jar` to the ABSOLUTE PATH of your
jfxrt.jar file as well.

```sh
mvn -Djavafx-runtime-jar=/path/to/your/jfxrt.jar install
```