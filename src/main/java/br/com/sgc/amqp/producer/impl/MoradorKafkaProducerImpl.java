package br.com.sgc.amqp.producer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.dto.MoradorDto;

@Component
public class MoradorKafkaProducerImpl implements AmqpProducer<MoradorDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(MoradorKafkaProducerImpl.class);
	
	private final String topic;
	
	private final KafkaTemplate<String, MoradorDto> kafkaTemplate;

	public MoradorKafkaProducerImpl(@Value("${morador.topic.name}") String topic, KafkaTemplate<String, MoradorDto> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(MoradorDto dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> logger.info("Message send " + success.getProducerRecord().value()),
				failure -> logger.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

