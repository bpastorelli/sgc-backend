package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.VisitaAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.EncerraVisitaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitaDto;
import br.com.sgc.entities.Visita;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VisitaMapper;
import br.com.sgc.repositories.VisitaRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitaAMQPImpl implements AmqpService<VisitaDto, EncerraVisitaDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<VisitaAvro> amqp;
	
	@Autowired
	private Validators<VisitaDto, EncerraVisitaDto> validator;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private VisitaMapper visitaMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(VisitaDto visitaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veículo: {}", visitaRequestBody.toString());
		
		visitaRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPost(visitaRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitaRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(this.visitaMapper.visitaDtoToVisitaAvro(visitaRequestBody));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitaRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	@Override
	public ResponsePublisherDto sendToConsumerPut(EncerraVisitaDto visitaRequestBody) throws RegistroException {

		log.info("Cadastrando um veículo: {}", visitaRequestBody.toString());
		
		this.validator.validarPut(visitaRequestBody);
		
		Visita visita = visitaRepository.findById(visitaRequestBody.getId()).get();
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitaRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(this.visitaMapper.visitaToVisitaAvro(visita));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visita.getGuide())
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
			if(this.visitaRepository.findByGuide(guide).isPresent())
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
	public VisitaDto mergeObject(VisitaDto t, EncerraVisitaDto x) {
		// TODO Auto-generated method stub
		return null;
	}

}
