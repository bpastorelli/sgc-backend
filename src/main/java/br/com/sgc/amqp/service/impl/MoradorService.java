package br.com.sgc.amqp.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sgc.amqp.producer.impl.MoradorProducer;
import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.CabecalhoResponsePublisherDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("moradorService")
public class MoradorService implements AmqpService<MoradorDto, AtualizaMoradorDto> {
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private MoradorProducer producer;
	
	@Autowired
	private Validators<MoradorDto, AtualizaMoradorDto> validator;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	@Override
	public ResponsePublisherDto sendToConsumerPost(MoradorDto moradorRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", moradorRequestBody.toString());
		
		moradorRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPost(moradorRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  moradorRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(moradorMapper.moradorDtoToMoradorAvro(moradorRequestBody));
		
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
		
		this.validator.validarPut(moradorRequestBody, id);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  moradorRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(moradorMapper.moradorDtoToMoradorAvro(this.mergeObject(this.moradorMapper.moradorToMoradorDto(moradorRepository.findById(id).get()), moradorRequestBody)));
		
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
