package org.appsdeveloperblog.ws.emailnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class EmailNotificationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailNotificationMicroserviceApplication.class, args);
	}

}
