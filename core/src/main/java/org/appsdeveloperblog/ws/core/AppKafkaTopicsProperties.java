package org.appsdeveloperblog.ws.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.topics")
public class AppKafkaTopicsProperties {
    private String productCreatedEvents;
    private int partitions = 3;
    private short replicas = 3;
    private int minInsyncReplicas = 2;

    public Map<String, String> getConfigs() {
        return Map.of("min.insync.replicas",
                String.valueOf(minInsyncReplicas)
                );
    }
}
