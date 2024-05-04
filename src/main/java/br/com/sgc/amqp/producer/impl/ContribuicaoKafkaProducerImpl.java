package br.com.sgc.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.sgc.ContribuicaoAvro;
import br.com.sgc.amqp.producer.KafkaTemplateAbstract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ContribuicaoKafkaProducerImpl extends KafkaTemplateAbstract<ContribuicaoAvro> {
	
	@Value("${contribuicao.topic.name}")
	private String topic;
	
	@Override
	public void producer(ContribuicaoAvro dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Message send " + success.getProducerRecord().value()),
				failure -> log.info("Message failure " + failure.getMessage())
		);	
		
	}

	@Async
	@Override
	public void producerAsync(ContribuicaoAvro dto) {
		
		
		try {
			
			Runnable runnable = () -> kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

				@Override
				public void onSuccess(SendResult<String, ContribuicaoAvro> result) {
					
					log.info("Mensagem enviada.");
					
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
			log.info("Finalizando a Thread {}", Thread.currentThread().getName());
			stopThread();
		}
		
		log.info("Finalizado.");
		
	}

}
