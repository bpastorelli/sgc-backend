package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.EncerraVisitaAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.EncerraVisitaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.entities.Visita;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VisitaMapper;
import br.com.sgc.repositories.VisitaRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EncerrarVisitaAMQPImpl implements AmqpService<EncerraVisitaDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<EncerraVisitaAvro> amqp;
	
	@Autowired
	private Validators<EncerraVisitaDto> validator;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private VisitaMapper visitaMapper;
	
	@Override
	public ResponsePublisherDto sendToConsumer(EncerraVisitaDto t) throws RegistroException {

		log.info("Cadastrando um veículo: {}", t.toString());
		
		this.validator.validar(t);
		
		Visita visita = visitaRepository.findById(t.getId()).get();
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  t.toString() + " para o consumer.");
		
		this.amqp.producer(this.visitaMapper.encerravisitaDtoToEncerraVisitaAvro(t));
		
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

}
