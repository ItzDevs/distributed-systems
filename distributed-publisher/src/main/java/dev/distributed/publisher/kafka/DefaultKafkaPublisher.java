package dev.distributed.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "DefaultKafkaPublisher")
public class DefaultKafkaPublisher extends DefaultKafkaProducerFactory<String, String> {
    public DefaultKafkaPublisher(Map<String, Object> configs) {
        super(getConfigs(configs));
    }

    // Adds the serializers used.
    private static Map<String, Object> getConfigs(Map<String, Object> configs) {
        Map<String, Object> result = new HashMap<>(configs);

        result.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        result.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return result;
    }
}
