package br.com.sgc.amqp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.MoradorAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorServiceAMQPImpl implements AmqpService<MoradorDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<MoradorAvro> amqp;
	
	@Autowired
	private Validators<List<MoradorDto>> validator;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	
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
		
		this.amqp.producer(moradorMapper.moradorDtoToMoradorPostAvro(moradorRequestBody));
		
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
