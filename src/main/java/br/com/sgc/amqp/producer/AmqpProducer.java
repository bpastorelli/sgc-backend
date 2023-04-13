package br.com.sgc.amqp.producer;

public interface AmqpProducer<T> {
	
	void producer(T t);
	
	void producerAsync(T t);
}		
