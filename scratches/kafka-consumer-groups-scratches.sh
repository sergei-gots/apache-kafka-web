# Groups list
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--list

# Delete a group
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--delete --group email-notifications-product-created-events

# Description for a group
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--describe --group email-notifications-product-created-events
