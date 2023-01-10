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
import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorServiceAMQPImpl implements AmqpService<MoradorDto, AtualizaMoradorDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private AmqpProducer<MoradorAvro> amqp;
	
	@Autowired
	private Validators<List<MoradorDto>, List<AtualizaMoradorDto>> validator;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(MoradorDto moradorRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", moradorRequestBody.toString());
		
		moradorRequestBody.setGuide(this.gerarGuide()); 	
		
		List<MoradorDto> listMorador = new ArrayList<MoradorDto>();
		
		listMorador.add(moradorRequestBody);
		
		this.validator.validarPost(listMorador);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  moradorRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(moradorMapper.moradorDtoToMoradorAvro(moradorRequestBody));
		
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
	public ResponsePublisherDto sendToConsumerPut(AtualizaMoradorDto moradorRequestBody, Long id) throws RegistroException {

		log.info("Atualizando um morador: {}", moradorRequestBody.toString());
		
		moradorRequestBody.setGuide(this.gerarGuide()); 	
		
		List<AtualizaMoradorDto> listMorador = new ArrayList<AtualizaMoradorDto>();
		
		listMorador.add(moradorRequestBody);
		
		this.validator.validarPut(listMorador, id);
		
		//Prepara os dados para enviar para a fila.
		Morador morador = moradorRepository.findById(moradorRequestBody.getId()).get();
		MoradorDto moradorDto = this.moradorMapper.moradorToMoradorDto(morador);
		moradorDto = this.mergeObject(moradorDto, moradorRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  moradorRequestBody.toString() + " para o consumer.");
		
		this.amqp.producer(moradorMapper.moradorDtoToMoradorAvro(moradorDto));
		
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

	@Override
	public MoradorDto mergeObject(MoradorDto t, AtualizaMoradorDto x) {
		
		t.setNome(x.getNome());
		t.setEmail(x.getEmail());
		t.setRg(x.getRg());
		t.setTelefone(x.getTelefone());
		t.setCelular(x.getCelular());
		t.setPerfil(x.getPerfil());
		t.setResidenciaId(x.getResidenciaId());
		t.setAssociado(x.getAssociado());
		t.setPosicao(x.getPosicao());
		t.setGuide(x.getGuide());
		
		return t;
	}

}
