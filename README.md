# coverage-example-java

Example repository on how to generate a coverage report for Java.

## Requirements

- Maven 3.x
- Java 8

If you don't have those specific versions, [SDKMAN!](https://sdkman.io/install) can be helpful.

## Run tests

```bash
mvn clean test
```

Running tests will also generate a [JaCoCo](https://www.eclemma.org/jacoco/) report. After successfully running the
tests, you can find the XML report at `target/site/jacoco/jacoco.xml`.
