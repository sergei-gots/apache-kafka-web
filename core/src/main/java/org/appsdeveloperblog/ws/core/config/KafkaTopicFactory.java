package org.appsdeveloperblog.ws.core.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicFactory {

    private final KafkaTopicsProperties props;

    public KafkaTopicFactory(KafkaTopicsProperties props) {
        this.props = props;
    }

    public NewTopic createTopic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(props.getPartitions())
                .replicas(props.getReplicas())
                .configs(props.getConfigs())
                .build();
    }
}

