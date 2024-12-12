# coverage-example-java

Commit to test

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2b3f4c14ccda403dbc9d3b704ad3e4e7)](https://app.codacy.com/gh/troubleshoot-codacy/coverage-example-java/dashboard)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/2b3f4c14ccda403dbc9d3b704ad3e4e7)](https://app.codacy.com/gh/troubleshoot-codacy/coverage-example-java/coverage/dashboard)

Example repository on how to generate a coverage report for Java!!

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
