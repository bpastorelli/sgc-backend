package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import br.com.sgc.VinculoResidenciaAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VinculoResidenciaKafkaProducerImpl extends KafkaTemplateAbstract<VinculoResidenciaAvro> {
	
	private final String topic;

	public VinculoResidenciaKafkaProducerImpl(@Value("${vinculo.topic.name}") String topic, KafkaTemplate<String, VinculoResidenciaAvro> kafkaTemplate) {
		
		this.topic = topic;
		this.kafkaTemplate = kafkaTemplate;
		
	}
	
	@Override
	public void producer(VinculoResidenciaAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

