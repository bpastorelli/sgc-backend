package br.com.sgc.amqp;

public interface AmqpProducer<T> {
	void send(T dto);
}
