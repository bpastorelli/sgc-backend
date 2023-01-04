package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.VinculoResidenciaAvro;
import br.com.sgc.amqp.producer.AmqpProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaVinculoResidenciaDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VinculoResidenciaDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VinculoResidenciaMapper;
import br.com.sgc.repositories.VinculoResidenciaRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VinculoResidenciaAMQPImpl implements AmqpService<VinculoResidenciaDto, AtualizaVinculoResidenciaDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<VinculoResidenciaAvro> amqp;
	
	@Autowired
	private Validators<VinculoResidenciaDto, AtualizaVinculoResidenciaDto> validator;
	
	@Autowired
	private VinculoResidenciaRepository vinculoRepository;
	
	@Autowired
	private VinculoResidenciaMapper vinculoMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(VinculoResidenciaDto vinculoRequestBody) throws RegistroException {
		
		log.info("Cadastrando um vinculo: {}", vinculoRequestBody.toString());
		
		vinculoRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPost(vinculoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  vinculoRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(this.vinculoMapper.vinculoResidenciaDtoToVinculoResidenciaAvro(vinculoRequestBody));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(vinculoRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	@Override
	public ResponsePublisherDto sendToConsumerPut(AtualizaVinculoResidenciaDto x) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gerarGuide() {

		String guide = null;
		int i = 0;
		boolean ticketValido = false;
		
		do {
			i++;
			if(this.vinculoRepository.findByGuide(guide).isPresent())
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
	public VinculoResidenciaDto mergeObject(VinculoResidenciaDto t, AtualizaVinculoResidenciaDto x) {
		// TODO Auto-generated method stub
		return null;
	}

}
