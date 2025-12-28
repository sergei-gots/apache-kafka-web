# To view messages in a Kafka topic
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic \
 --group product-created-events-topic-debug-console \
 --property print.key=true \

# To view all the messages in a Kafka topic
# Note: it will create a new consumer group
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic \
 --property print.key=true \
 --from-beginning

# To view the messages in a Dead Letter Topic in Kafka
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic-dlt \
 --group product-created-events-topic-dlt-debug-console \
 --property print.key=true \

# To view all the messages in a Dead Letter Topic in Kafka.
# Note: it will create a new consumer group
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic-dlt \
 --property print.key=true \
 --from-beginning
