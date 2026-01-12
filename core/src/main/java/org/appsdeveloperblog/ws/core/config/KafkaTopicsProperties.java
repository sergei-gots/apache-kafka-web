package org.appsdeveloperblog.ws.core.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.topics")
public class KafkaTopicsProperties {

    private int partitions = 3;
    private short replicas = 3;
    private int minInsyncReplicas = 2;

    public Map<String, String> getConfigs() {
        return Map.of("min.insync.replicas",
                String.valueOf(minInsyncReplicas)
                );
    }

    public NewTopic buildTopic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .configs(getConfigs())
                .build();
    }

}
