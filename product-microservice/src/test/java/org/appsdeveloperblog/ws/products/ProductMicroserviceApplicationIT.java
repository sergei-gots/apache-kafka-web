package org.appsdeveloperblog.ws.products;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

/**
 *  To run this integration test  (IT) separately use 'failsafe'-plugin applying this command:
 *   <code>./mvnw clean verify -Dtest=org.appsdeveloperblog.ws.products.ProductMicroserviceApplicationTests -X<code>
 *   '-X' specifies the DEBUG MODE output here.
 */
@EmbeddedKafka(partitions = 1, // partitions = 3, count = 3,
        controlledShutdown = true,
        brokerProperties = {
                "process.roles=broker,controller",  // combined
                "node.id=1",
                "controller.quorum.voters=1@localhost:0"  // 0 = random port
        }
)
@SpringBootTest
class ProductMicroserviceApplicationIT {

	@Test
	void contextLoads() {
	}

}
