package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.VisitanteAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VisitanteMapper;
import br.com.sgc.repositories.VisitanteRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitanteAMQPImpl implements AmqpService<VisitanteDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<VisitanteAvro> amqp;
	
	@Autowired
	private Validators<VisitanteDto> validator;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private VisitanteMapper visitanteMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(VisitanteDto visitanteRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veículo: {}", visitanteRequestBody.toString());
		
		visitanteRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validar(visitanteRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitanteRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(this.visitanteMapper.visitanteDtoToVisitanteAvro(visitanteRequestBody));
		
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

}
