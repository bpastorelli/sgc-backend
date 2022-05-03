package br.com.sgc.amqp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.utils.Guide;
import br.com.sgc.validators.Validators;


@Service
public class MoradorServiceAMQPImpl implements AmqpService<MoradorDto> {
	
	private static final Logger log = LoggerFactory.getLogger(MoradorServiceAMQPImpl.class);
	
	@Value("${guide.size}")
	private int guideSize;
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<MoradorDto> amqp;
	
	@Autowired
	private Validators<List<MoradorDto>> validator;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(MoradorDto moradorRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", moradorRequestBody.toString());
		
		moradorRequestBody.setGuide(this.gerarGuide()); 	
		
		List<MoradorDto> listMorador = new ArrayList<MoradorDto>();
		
		listMorador.add(moradorRequestBody);
		
		List<ErroRegistro> errors = this.validator.validar(listMorador);
		
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
		log.info("Enviando mensagem " +  moradorRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(moradorRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(moradorRequestBody.getGuide())
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
			if(this.moradorRepository.findByGuide(guide).isPresent())
				guide = Guide.gerar(guideSize);
			else if(guide == null)
				guide = Guide.gerar(guideSize);
			else {
				ticketValido = true;
			}
			
		}while(!ticketValido && i < guideLimit);
		
		return guide;
		
	}

}
