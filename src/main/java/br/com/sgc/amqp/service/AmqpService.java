package br.com.sgc.amqp.service;

import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;

public interface AmqpService<T> {
	
	public ResponsePublisherDto sendToConsumer(T t) throws RegistroException;
	
	public String gerarGuide();
	
}
