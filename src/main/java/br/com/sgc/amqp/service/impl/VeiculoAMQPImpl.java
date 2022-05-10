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
import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VeiculoMapper;
import br.com.sgc.repositories.VeiculoRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeiculoAMQPImpl implements AmqpService<VeiculoDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<VeiculoAvro> amqp;
	
	@Autowired
	private Validators<VeiculoDto> validator;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumer(VeiculoDto veiculoRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veículo: {}", veiculoRequestBody.toString());
		
		veiculoRequestBody.setGuide(this.gerarGuide()); 	
		
		List<ErroRegistro> errors = this.validator.validar(veiculoRequestBody);
		
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
		log.info("Enviando mensagem " +  veiculoRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(this.veiculoMapper.veiculoDtoToVeiculoAvro(veiculoRequestBody));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(veiculoRequestBody.getGuide())
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
			if(this.veiculoRepository.findByGuide(guide).isPresent())
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
