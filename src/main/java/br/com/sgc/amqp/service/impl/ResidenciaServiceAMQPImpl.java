package br.com.sgc.amqp.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.validators.Validators;


@Service
public class ResidenciaServiceAMQPImpl implements AmqpService<ResidenciaDto> {
	
	private static final Logger log = LoggerFactory.getLogger(ResidenciaServiceAMQPImpl.class);
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<ResidenciaDto> amqp;
	
	@Autowired
	private Validators<ResidenciaDto> validator;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(ResidenciaDto residenciaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(this.gerarGuide()); 	
		
		List<ErroRegistro> errors = this.validator.validar(residenciaRequestBody);
		
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
		log.info("Enviando mensagem " +  residenciaRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(residenciaRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(residenciaRequestBody.getGuide())
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
			if(this.residenciaRepository.findByGuide(guide).isPresent())
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
