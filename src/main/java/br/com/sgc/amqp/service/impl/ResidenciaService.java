package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.impl.ResidenciaProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.ResidenciaMapper;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaService implements AmqpService<ResidenciaDto, AtualizaResidenciaDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private ResidenciaProducer producer;
	
	@Autowired
	private Validators<ResidenciaDto, AtualizaResidenciaDto> validator;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(ResidenciaDto residenciaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(this.gerarGuide());
		
		this.validator.validarPost(residenciaRequestBody);
		
		//Preenche o id da residencia se ela já existir, para fazer apenas vinculo do morador.
		residenciaRepository.findByCepAndNumeroAndComplemento(residenciaRequestBody.getCep(), residenciaRequestBody.getNumero(), residenciaRequestBody.getComplemento())
			.ifPresent(r -> {
				residenciaRequestBody.setId(r.getId());
				residenciaRequestBody.setGuide(r.getGuide());
			});
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  residenciaRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.residenciaMapper.residenciaDtoToResidenciaAvro(residenciaRequestBody));
		
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
	public ResponsePublisherDto sendToConsumerPut(AtualizaResidenciaDto residenciaRequestBody, Long id) throws RegistroException {

		log.info("Atualizando uma residencia: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPut(residenciaRequestBody, id);
		
		//Prepara os dados para enviar para a fila.
		Residencia residencia = residenciaRepository.findById(residenciaRequestBody.getId()).get();
		ResidenciaDto residenciaDto = this.residenciaMapper.residenciaToResidenciaDto(residencia);
		residenciaDto = this.mergeObject(residenciaDto, residenciaRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  residenciaRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(this.residenciaMapper.residenciaDtoToResidenciaAvro(residenciaDto));
		
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

	@Override
	public ResidenciaDto mergeObject(ResidenciaDto t, AtualizaResidenciaDto x) {
		
		t.setEndereco(x.getEndereco());
		t.setNumero(x.getNumero());
		t.setComplemento(x.getComplemento());
		t.setBairro(x.getBairro());
		t.setCep(x.getCep());
		t.setCidade(x.getCidade());
		t.setUf(x.getUf());
		
		return t;
	}

}
