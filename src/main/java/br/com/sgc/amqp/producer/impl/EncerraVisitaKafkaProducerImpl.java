package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.sgc.EncerraVisitaAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EncerraVisitaKafkaProducerImpl extends KafkaTemplateAbstract<EncerraVisitaAvro> {
	
	@Value("${visita.topic.name}")
	private String topic;
	
	@Override
	public void producer(EncerraVisitaAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}

	@Async
	@Override
	public void producerAsync(EncerraVisitaAvro dto) {
		
		Runnable runnable = () -> kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

			@Override
			public void onSuccess(SendResult<String, EncerraVisitaAvro> result) {
				
				log.info("Mensagem enviada: " + result.getProducerRecord().value());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				
				if(ex != null)
					log.error(ex.getMessage());
				
			}

        });
	    new Thread(runnable).start();
		
	}

}
