# Publish a message to product-created-events-topic
echo '"5f308922-ff7f-4bb5-bc07-98fc8585b111";{"title":"iPhone-19","quantity":5,"price":350,"productId":"5f308922-ff7f-4bb5-bc07-98fc8585b111"}' | \
./kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic product-created-events-topic \
  --property parse.key=true \
  --property key.separator=";" \
  --property headers="__TypeId__:com.appsdeveloperblog.ws.core.ProductCreatedEvent"

# Start kafka-console-producer.sh for the topic product-created-events-topic
./kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic product-created-events-topic \
  --property parse.key=true \
  --property key.separator=";" \
  --property headers="__TypeId__:com.appsdeveloperblog.ws.core.ProductCreatedEvent"


# Reset offset for the group product-created-events for the topic product-created-events-topic
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group product-created-events --reset-offsets --topic product-created-events-topic --to-latest --execute


