package br.com.sgc.amqp.producer;

public interface AmqpProducer<T> {
	
	void producer(T dto);
	
	void producerAsync(T dto);
	
	default void stopThread() {
		
		Thread.currentThread().isInterrupted();
	}
}		
