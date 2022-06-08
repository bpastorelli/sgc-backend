package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.ProcessoCadastroAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessoCadastroServiceAMQPImpl implements AmqpService<ProcessoCadastroDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<ProcessoCadastroAvro> amqp;
	
	@Autowired
	private Validators<ProcessoCadastroDto> validator;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(ProcessoCadastroDto processoRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", processoRequestBody.toString());
		
		processoRequestBody.setGuide(this.gerarGuide());
		
		this.validator.validar(processoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  processoRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(moradorMapper.processoDtoToProcessoAvro(processoRequestBody));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(processoRequestBody.getGuide())
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

