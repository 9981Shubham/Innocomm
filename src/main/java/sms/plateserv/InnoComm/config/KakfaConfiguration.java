package sms.plateserv.InnoComm.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import sms.plateserv.InnoComm.model.SmsRequest;

@EnableKafka
@Configuration
public class KakfaConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(KakfaConfiguration.class);

	// Producer configuration
	@Bean
	public ProducerFactory<String, SmsRequest> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, SmsRequest> kafkaTemplate() {
		LOGGER.debug("Inside kafka template to register a bean of it");
		return new KafkaTemplate<>(producerFactory());
	}

	// consumer configuration
	@Bean
	public ConsumerFactory<String, SmsRequest> userConsumerFactory() {
		LOGGER.debug("Bean is defined for ConsumerFactory");
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_json");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
				new JsonDeserializer<>(SmsRequest.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, SmsRequest> userKafkaListenerFactory() {
		LOGGER.debug("Bean is defined for ConcurrentKafkaListenerContainerFactory");
		ConcurrentKafkaListenerContainerFactory<String, SmsRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userConsumerFactory());
		return factory;
	}
}