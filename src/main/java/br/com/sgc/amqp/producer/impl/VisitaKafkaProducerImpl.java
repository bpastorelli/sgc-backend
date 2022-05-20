package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.VisitaAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitaKafkaProducerImpl implements AmqpProducer<VisitaAvro> {
	
	private final String topic;
	
	private final KafkaTemplate<String, VisitaAvro> kafkaTemplate;

	public VisitaKafkaProducerImpl(@Value("${visita.topic.name}") String topic, KafkaTemplate<String, VisitaAvro> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(VisitaAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

