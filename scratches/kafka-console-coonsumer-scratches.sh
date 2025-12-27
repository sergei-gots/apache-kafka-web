# To view all the messages in a Kafka topic
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic \
 --property print.key=true \
 --from-beginning

# To view all the messages in a Dead Letter Topic in Kafka
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic-dlt \
 --property print.key=true \
 --from-beginning

