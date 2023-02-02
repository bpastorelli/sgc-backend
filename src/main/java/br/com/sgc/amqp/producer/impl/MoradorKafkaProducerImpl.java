package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.sgc.MoradorAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MoradorKafkaProducerImpl extends KafkaTemplateAbstract<MoradorAvro> {
	
	@Value("${morador.topic.name}")
	private String topic;
	
	@Override
	public void producer(MoradorAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

