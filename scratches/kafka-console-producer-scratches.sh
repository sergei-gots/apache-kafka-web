# Publish a single specified message to product-created-events-topic
echo '__TypeId__:org.appsdeveloperblog.ws.core.ProductCreatedEvent;5f308922-ff7f-4bb5-bc07-98fc8585b111;{"productId":"5f308922-ff7f-4bb5-bc07-98fc8585b111","title":"iPhone-19","quantity":5,"price":350}' | \
./kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic product-created-events-topic \
  --property parse.key=true \
  --property key.separator=';' \
  --property parse.headers=true  \
  --property headers.key.separator=':' \
  --property headers.delimiter=';'

# Start kafka-console-producer.sh for the topic product-created-events-topic
./kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic product-created-events-topic \
  --property parse.key=true \
  --property key.separator=';' \
  --property parse.headers=true  \
  --property headers.key.separator=':' \
  --property headers.delimiter=';'
  #Example input:
  #__TypeId__:org.appsdeveloperblog.ws.core.ProductCreatedEvent;5f308922-ff7f-4bb5-bc07-98fc8585b111;{"productId":"5f308922-ff7f-4bb5-bc07-98fc8585b111","title":"iPhone-26","quantity":5,"price":800}
  #__TypeId__:org.appsdeveloperblog.ws.core.ProductCreatedEvent;5f308922-ff7f-4bb5-bc07-98fc8585b112;{"productId":"5f308922-ff7f-4bb5-bc07-98fc8585b112","title":"iPhone-26","quantity":-1,"price":800}

# Reset offset for the group product-created-events for the topic product-created-events-topic
./kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --group product-created-events \
  --topic product-created-events-topic \
  --reset-offsets --to-latest --execute

# To view all the messages in a Dead Letter Topic in Kafka
 ./kafka-console-consumer.sh \
 --bootstrap-server localhost:9092 \
 --topic product-created-events-topic-dlt \
 --property print.key=true \
 --from-beginning

