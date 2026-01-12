# Apache Kafka study project

According to the course 'Apache Kafka for Spring Boot microservices'
https://www.udemy.com/course/apache-kafka-for-spring-boot-microservices/

## Project structure

The project consists of set of modules:  

- core  
- product-microservice
- email-notification-microservice  
- mock-http-service
- transfer-microservice
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

