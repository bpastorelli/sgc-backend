package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.impl.VisitanteKafkaProducerImpl;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaVisitanteDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.entities.Visitante;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VisitanteMapper;
import br.com.sgc.repositories.VisitanteRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitanteService implements AmqpService<VisitanteDto, AtualizaVisitanteDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private VisitanteKafkaProducerImpl producer;
	
	@Autowired
	private Validators<VisitanteDto, AtualizaVisitanteDto> validator;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private VisitanteMapper visitanteMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(VisitanteDto visitanteRequestBody) throws RegistroException {
		
		log.info("Cadastrando um visitante: {}", visitanteRequestBody.toString());
		
		visitanteRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPost(visitanteRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitanteRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.visitanteMapper.visitanteDtoToVisitanteAvro(visitanteRequestBody));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitanteRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	@Override
	public ResponsePublisherDto sendToConsumerPut(AtualizaVisitanteDto visitanteRequestBody, Long id) throws RegistroException {

		log.info("Atualizando um visitante: {}", visitanteRequestBody.toString());
		
		visitanteRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPut(visitanteRequestBody, id);
		
		//Prepara os dados para enviar para a fila.
		Visitante visitante = visitanteRepository.findById(visitanteRequestBody.getId()).get();
		VisitanteDto visitanteDto = this.visitanteMapper.visitanteToVisitanteDto(visitante);
		visitanteDto = this.mergeObject(visitanteDto, visitanteRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitanteRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.visitanteMapper.visitanteDtoToVisitanteAvro(visitanteDto));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitanteRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}

	@Override
	public String gerarGuide() {

		String guide = null;
		int i = 0;
		boolean ticketValido = false;
		
		do {
			i++;
			if(this.visitanteRepository.findByGuide(guide).isPresent())
				guide = UUID.randomUUID().toString();
			else if(guide == null)
				guide = UUID.randomUUID().toString();
			else {
				ticketValido = true;
			}
			
		}while(!ticketValido && i < guideLimit);
		
		return guide;
	}

	@Override
	public VisitanteDto mergeObject(VisitanteDto t, AtualizaVisitanteDto x) {
		

		t.setNome(x.getNome());	
		t.setCpf(x.getCpf());
		t.setCep(x.getCep());
		t.setEndereco(x.getEndereco());
		t.setNumero(x.getNumero());
		t.setComplemento(x.getComplemento());
		t.setBairro(x.getBairro());
		t.setCidade(x.getCidade());
		t.setUf(x.getUf());
		t.setTelefone(x.getTelefone());
		t.setCelular(x.getCelular());
		t.setPosicao(x.getPosicao());
		
		return t;
	}

}
