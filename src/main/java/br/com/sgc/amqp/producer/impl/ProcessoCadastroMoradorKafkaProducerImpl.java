package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.sgc.ProcessoCadastroAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProcessoCadastroMoradorKafkaProducerImpl extends KafkaTemplateAbstract<ProcessoCadastroAvro> {
	
	@Value("${processo.topic.name}")
	private String topic;
	
	@Override
	public void producer(ProcessoCadastroAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}
	
}

