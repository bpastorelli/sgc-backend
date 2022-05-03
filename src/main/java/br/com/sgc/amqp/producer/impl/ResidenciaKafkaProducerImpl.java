package br.com.sgc.amqp.producer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.dto.ResidenciaDto;

@Component
public class ResidenciaKafkaProducerImpl implements AmqpProducer<ResidenciaDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(ResidenciaKafkaProducerImpl.class);
	
	private final String topic;
	
	private final KafkaTemplate<String, ResidenciaDto> kafkaTemplate;

	public ResidenciaKafkaProducerImpl(@Value("${residencia.topic.name}") String topic, KafkaTemplate<String, ResidenciaDto> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(ResidenciaDto dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> logger.info("Message send " + success.getProducerRecord().value()),
				failure -> logger.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

