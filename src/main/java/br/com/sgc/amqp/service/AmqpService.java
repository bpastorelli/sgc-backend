package br.com.sgc.amqp.service;

import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;

public interface AmqpService<T, X> {
	
	public ResponsePublisherDto sendToConsumerPost(T t) throws RegistroException;
	
	public ResponsePublisherDto sendToConsumerPut(X x) throws RegistroException;
	
	public T mergeObject(T t, X x);
	
	public String gerarGuide();
	
}
