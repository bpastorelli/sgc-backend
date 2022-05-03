package br.com.sgc.amqp.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResidenciaDto;


@Configuration
public class KafkaProducerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootStrapAddress;
	
	@Value(value = "${topic.morador.name}")
	private String topicMorador;
	
	@Value(value = "${topic.residencia.name}")
	private String topicResidencia;
	
	@Bean
	public NewTopic createMoradorTopic() {
		
		return new NewTopic(topicMorador, 3,(short) 1);
	}
	
	@Bean
	public NewTopic createResidenciaTopic() {
		
		return new NewTopic(topicResidencia, 3,(short) 1);
	}
	
	@Bean
	public ProducerFactory<String, MoradorDto> moradorProducerFactory(){
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public ProducerFactory<String, ResidenciaDto> residenciaProducerFactory(){
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<String, MoradorDto> moradorKafkaTemplate(){
		return new KafkaTemplate<>(moradorProducerFactory());
	}
	
	@Bean
	public KafkaTemplate<String, ResidenciaDto> residenciaKafkaTemplate(){
		return new KafkaTemplate<>(residenciaProducerFactory());
	}
	
}
