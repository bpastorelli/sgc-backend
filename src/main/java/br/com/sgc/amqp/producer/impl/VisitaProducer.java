package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.sgc.VisitaAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitaProducer extends KafkaTemplateAbstract<VisitaAvro> {
	
	@Value("${visita.topic.name}")
	private String topic;
	
	public void producer(VisitaAvro dto) {
		
		ListenableFuture<SendResult<String, VisitaAvro>> future = kafkaTemplate.send(topic, dto);
		
	    future.addCallback(new ListenableFutureCallback<SendResult<String, VisitaAvro>>() {

			@Override
			public void onSuccess(SendResult<String, VisitaAvro> result) {
				
				log.info("Mensagem enviada: " + result.getProducerRecord().value());
			}

			@Override
			public void onFailure(Throwable ex) {
				
				if(ex != null)
					log.error(ex.getMessage());
				
			}

	    });	
		
	}

	@Async("asyncKafka")
	public void producerAsync(VisitaAvro dto) {
		
		try {
			
			Runnable runnable = () -> kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

				@Override
				public void onSuccess(SendResult<String, VisitaAvro> result) {
					
					log.info("Mensagem enviada: " + result.getProducerRecord().value());
					
				}

				@Override
				public void onFailure(Throwable ex) {
					
					if(ex != null)
						log.error("Erro: " + ex.getMessage());
					
				}

	        });
		    new Thread(runnable).start();
			
		}catch(Exception ex) {
			
			log.error("Catch Erro: " + ex.getMessage());
			
		}finally{
			log.info("Finalizando a Thread...");
			Thread.currentThread().isInterrupted();
		}
		
	}
	
}

