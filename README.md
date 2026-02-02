# Apache Kafka workout project

This project is inspired by the course **"Apache Kafka for Spring Boot Microservices"**,  
but rebuilt on the latest stable versions: **Spring Boot 4.0** + **Spring Framework 7.0** (as of early 2026).

It uses up-to-date dependencies, including the modern Kafka library stack (as of December 2025).

Special attention is given to clean, high-level Kafka configuration using `@Configuration` classes and modern Spring Boot best practices.
https://www.udemy.com/course/apache-kafka-for-spring-boot-microservices/

## Project structure

The project consists of set of modules:  

- core  
- product-microservice
- email-notification-microservice  
- mock-http-service
- transfer-microservice
- deposit-microservice
- withdrawal-microservice

The <code>core</code> module contains a code used in all the rest modules
The <code>mock-http-service</code> is a tiny http service used to accept http requests sent
by other modules
All the rest modules with the postfix <code>-microservice</code> are the microservices. 


## POM
### main pom.xml 

The main pom.xml file (in the project root directory)
contains module declarations: 

    <modules>
        <module>core</module>
        <module>mock-http-service</module>
        <module>product-microservice</module>
        <module>email-notification-microservice</module>
        <module>transfer-microservice</module>
        <module>deposit-microservice</module>
        <module>withdrawal-microservice</module>
    </modules>

### pom.xml in module
Each module pom.xml refers to the project main pom.xml file:

    <parent>
        <groupId>org.appsdeveloperblog.ws</groupId>
        <artifactId>apps-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

And also uses <code>java.version<code> out of there:

    <properties>
		<java.version>${java.version}</java.version>
	</properties>

The section <code><dependencies></code> of a service module pom.xml should include the reference
to the <code>core</code> module if it is necessary:

     <dependency>
            <groupId>org.appsdeveloperblog.ws</groupId>
            <artifactId>core</artifactId>
            <version>0.0.1-SNAPSHOT</version>
      </dependency>

