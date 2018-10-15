# pay-java-commons

This project contains common code for use across GOV.UK Pay's Java projects. 
Modules under this project are published to [bintray](https://bintray.com/govuk-pay/pay-java-commons/pay-java-commons/).
The latest version numbers can be found there. 

To include the testing module, add this to your project's pom.xml:

```xml
 <dependency>
   <groupId>uk.gov.pay</groupId>
   <artifactId>testing</artifactId>
   <version>1.0.0-{version}</version>
   <scope>test</scope>
 </dependency>
```

To include the utils module, add this to your project's pom.xml:
                             
```xml
 <dependency>
   <groupId>uk.gov.pay</groupId>
   <artifactId>utils</artifactId>
   <version>1.0.0-{version}</version>
 </dependency>
```