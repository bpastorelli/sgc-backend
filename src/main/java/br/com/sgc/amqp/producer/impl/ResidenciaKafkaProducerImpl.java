package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.ResidenciaAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResidenciaKafkaProducerImpl implements AmqpProducer<ResidenciaAvro> {
	
	private final String topic;
	
	private final KafkaTemplate<String, ResidenciaAvro> kafkaTemplate;

	public ResidenciaKafkaProducerImpl(@Value("${residencia.topic.name}") String topic, KafkaTemplate<String, ResidenciaAvro> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(ResidenciaAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

