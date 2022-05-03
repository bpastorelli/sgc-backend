package br.com.sgc.amqp.producer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.amqp.AmqpProducer;
import br.com.sgc.dto.MoradorDto;

@Component
public class MoradorKafkaProducerImpl implements AmqpProducer<MoradorDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(MoradorKafkaProducerImpl.class);
	
	@Value("${topic.morador.name}")
	private String topic;
	
	private KafkaTemplate<String, MoradorDto> kafkaTemplate;

	@Override
	public void send(MoradorDto dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> logger.info("Message send " + success.getProducerRecord().value()),
				failure -> logger.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

