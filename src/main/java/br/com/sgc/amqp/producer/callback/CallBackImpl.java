package br.com.sgc.amqp.producer.callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CallBackImpl implements Callback {

	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		
		if (exception != null) {
			exception.printStackTrace();
		}
		
	}

}
