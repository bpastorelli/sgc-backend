package br.com.sgc.amqp.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitaDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VisitaMapper;
import br.com.sgc.repositories.VisitaRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitaAMQPImpl implements AmqpService<VisitaDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<VeiculoAvro> amqp;
	
	@Autowired
	private Validators<VisitaDto> validator;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private VisitaMapper visitaMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(VisitaDto visitaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veículo: {}", visitaRequestBody.toString());
		
		visitaRequestBody.setGuide(this.gerarGuide()); 	
		
		List<ErroRegistro> errors = this.validator.validar(visitaRequestBody);
		
		final ResponsePublisherDto responseError = new ResponsePublisherDto();
		
		if(errors.size() > 0) {			
			errors.forEach(error -> responseError.getErrors().add(
					new ErroRegistro(
					(String) error.getCodigo(), 
					(String) error.getTitulo(), 
					(String) error.getDetalhe())));
			
			return responseError;
		}
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitaRequestBody.toString() + " para o consumer.");
		
		//this.amqp.producer(this.visitaMapper.visitaDtoToVisitaAvro(visitaRequestBody));
		
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
