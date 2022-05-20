package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.VisitanteAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitanteKafkaProducerImpl implements AmqpProducer<VisitanteAvro> {
	
	private final String topic;
	
	private final KafkaTemplate<String, VisitanteAvro> kafkaTemplate;

	public VisitanteKafkaProducerImpl(@Value("${visitante.topic.name}") String topic, KafkaTemplate<String, VisitanteAvro> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(VisitanteAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

