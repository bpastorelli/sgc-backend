package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.impl.VeiculoProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaVeiculoDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.entities.Veiculo;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.VeiculoMapper;
import br.com.sgc.repositories.VeiculoRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeiculoService implements AmqpService<VeiculoDto, AtualizaVeiculoDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private VeiculoProducer producer;
	
	@Autowired
	private Validators<VeiculoDto, AtualizaVeiculoDto> validator;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(VeiculoDto veiculoRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veículo: {}", veiculoRequestBody.toString());
		
		veiculoRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPost(veiculoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  veiculoRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.veiculoMapper.veiculoDtoToVeiculoAvro(veiculoRequestBody));
		
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
	public ResponsePublisherDto sendToConsumerPut(AtualizaVeiculoDto veiculoRequestBody, Long id) throws RegistroException {
		
		log.info("Atualizando um veículo: {}", veiculoRequestBody.toString()); 	
		
		this.validator.validarPut(veiculoRequestBody, veiculoRequestBody.getId());
		
		//Prepara os dados para enviar para a fila.
		Veiculo veiculo = veiculoRepository.findById(id).get();
		VeiculoDto veiculoDto = this.veiculoMapper.veiculoToVeiculoDto(veiculo);
		veiculoDto = this.mergeObject(veiculoDto, veiculoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  veiculoRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.veiculoMapper.veiculoDtoToVeiculoAvro(veiculoDto));
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(veiculo.getGuide())
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

	@Override
	public VeiculoDto mergeObject(VeiculoDto t, AtualizaVeiculoDto x) {
		
		t.setMarca(x.getMarca());
		t.setModelo(x.getModelo());
		t.setCor(x.getCor());
		t.setAno(x.getAno());
		t.setPosicao(x.getPosicao());
		
		return t;
		
	}

}