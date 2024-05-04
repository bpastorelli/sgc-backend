package br.com.sgc.amqp.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaProducerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootStrapAddress;
	
	@Value(value = "${morador.topic.name}")
	private String topicMorador;
	
	@Value(value = "${processo.topic.name}")
	private String topicProcesso;
	
	@Value(value = "${residencia.topic.name}")
	private String topicResidencia;
	
	@Value(value = "${veiculo.topic.name}")
	private String topicVeiculo;
	
	@Value(value = "${visita.topic.name}")
	private String topicVisita;
	
	@Value(value = "${visitante.topic.name}")
	private String topicVisitante;
	
	@Value(value = "${vinculo.topic.name}")
	private String topicVinculo;
	
	@Value(value = "${contribuicao.topic.name}")
	private String topicContribuicao;	
	
	@Bean
	public NewTopic createMoradorTopic() {
		
		return new NewTopic(topicMorador, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createProcessoTopic() {
		
		return new NewTopic(topicProcesso, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createResidenciaTopic() {
		
		return new NewTopic(topicResidencia, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createVeiculoTopic() {
		
		return new NewTopic(topicVeiculo, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createVisitaTopic() {
		
		return new NewTopic(topicVisita, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createVisitanteTopic() {
		
		return new NewTopic(topicVisitante, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createVinculoTopic() {
		
		return new NewTopic(topicVinculo, 1,(short) 1);
	}
	
	@Bean
	public NewTopic createContribuicaoTopic() {
		
		return new NewTopic(topicContribuicao, 4,(short) 4);
	}
	
}
