package com.github.ethany.clickstream.configuration;

import clickstream.events;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class ClickStreamConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ClickStreamConfiguration.class.getName());
    @Value(value = "${spring.kafka.properties.bootstrap.servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;
    @Value(value = "${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;
    @Value(value = "${spring.kafka.properties.security.protocol}")
    private String securityProtocol;
    @Value(value = "${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;
    @Value(value = "${spring.kafka.properties.basic.auth.credentials.source}")
    private String basicAuthCredentialsSource;
    @Value(value = "${spring.kafka.properties.basic.auth.user.info}")
    private String basicAuthUserInfo;


    @Bean
    public ProducerFactory<String, events> producerFactory() {

        LOGGER.info("bootstrapAddress is: " + bootstrapAddress);
        LOGGER.info("schemaRegistryUrl is:  " + schemaRegistryUrl);

        return new DefaultKafkaProducerFactory<>(
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                        ProducerConfig.RETRIES_CONFIG, 0,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class,
                        KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl,
                        "basic.auth.user.info", basicAuthUserInfo,
                        "sasl.mechanism", saslMechanism,
                        "sasl.jaas.config", saslJaasConfig,
                        "security.protocol", securityProtocol,
                        "basic.auth.credentials.source", basicAuthCredentialsSource
                        )
        );
    }

    @Bean
    public KafkaTemplate<String, events> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
