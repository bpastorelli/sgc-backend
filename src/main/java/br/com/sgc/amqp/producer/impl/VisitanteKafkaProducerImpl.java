package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.sgc.VisitanteAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitanteKafkaProducerImpl extends KafkaTemplateAbstract<VisitanteAvro> {
	
	@Value("${visitante.topic.name}")
	private String topic;
	
	public void producer(VisitanteAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}

	@Async("asyncKafka")
	public void producerAsync(VisitanteAvro dto) {
		
		Runnable runnable = () -> kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

			@Override
			public void onSuccess(SendResult<String, VisitanteAvro> result) {
				
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

