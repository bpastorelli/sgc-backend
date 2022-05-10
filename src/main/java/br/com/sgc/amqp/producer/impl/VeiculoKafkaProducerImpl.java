package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VeiculoKafkaProducerImpl implements AmqpProducer<VeiculoAvro> {
	
	private final String topic;
	
	private final KafkaTemplate<String, VeiculoAvro> kafkaTemplate;

	public VeiculoKafkaProducerImpl(@Value("${veiculo.topic.name}") String topic, KafkaTemplate<String, VeiculoAvro> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(VeiculoAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

