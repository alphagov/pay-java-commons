# pay-java-commons
[![Release to maven](https://github.com/alphagov/pay-java-commons/actions/workflows/build-and-publish.yml/badge.svg)](https://github.com/alphagov/pay-java-commons/actions/workflows/build-and-publish.yml)

This project contains common code for use across GOV.UK Pay's Java projects. 
Modules under this project are published to [Maven Central](https://repo1.maven.org/maven2/uk/gov/service/payments/).
The latest version numbers can be found there. 

# Github Actions
The modules are updated via Github Actions, accessible in the [Actions](https://github.com/alphagov/pay-java-commons/actions) tab. These are automatic releases configured to push to Maven Central on merges to the `master` branch.

# Pom.xml Configuration

To include the testing module, add this to your project's pom.xml:

```
 <dependency>
   <groupId>uk.gov.service.payments</groupId>
   <artifactId>testing</artifactId>
   <version>1.0.0-{version}</version>
   <scope>test</scope>
 </dependency>
```

To include the utils module, add this to your project's pom.xml:
                             
```
 <dependency>
   <groupId>uk.gov.service.payments</groupId>
   <artifactId>utils</artifactId>
   <version>1.0.0-{version}</version>
 </dependency>
```

