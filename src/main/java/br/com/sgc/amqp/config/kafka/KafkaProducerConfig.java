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
	
	@Value(value = "${residencia.topic.name}")
	private String topicResidencia;
	
	@Value(value = "${veiculo.topic.name}")
	private String topicVeiculo;
	
	@Value(value = "${visita.topic.name}")
	private String topicVisita;
	
	@Value(value = "${visitante.topic.name}")
	private String topicVisitante;
	
	@Bean
	public NewTopic createMoradorTopic() {
		
		return new NewTopic(topicMorador, 3,(short) 1);
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
	
	/*@Bean
	public ProducerFactory<String, MoradorAvro> moradorProducerFactory(){
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
	public ProducerFactory<String, VeiculoDto> veiculoProducerFactory(){
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public ProducerFactory<String, VisitaDto> visitaProducerFactory(){
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<String, MoradorAvro> moradorKafkaTemplate(){
		return new KafkaTemplate<>(moradorProducerFactory());
	}
	
	@Bean
	public KafkaTemplate<String, ResidenciaDto> residenciaKafkaTemplate(){
		return new KafkaTemplate<>(residenciaProducerFactory());
	}
	
	@Bean
	public KafkaTemplate<String, VeiculoDto> veiculoKafkaTemplate(){
		return new KafkaTemplate<>(veiculoProducerFactory());
	}
	
	@Bean
	public KafkaTemplate<String, VisitaDto> visitaKafkaTemplate(){
		return new KafkaTemplate<>(visitaProducerFactory());
	}*/
	
}
