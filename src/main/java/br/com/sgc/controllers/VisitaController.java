package br.com.sgc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.EncerraVisitaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitaDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/visita")
@CrossOrigin(origins = "*")
class VisitaController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<VisitaDto> visitaAmqpService;
	
	@Autowired
	private AmqpService<EncerraVisitaDto> encerraVisitaAmqpService;
	
	public VisitaController() {
		
	}
	
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VisitaDto visitaRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitaAmqpService.sendToConsumer(visitaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@PutMapping(value = "/amqp/encerrar")
	public ResponseEntity<?> encerrarVisitaAMQP(@Valid @RequestBody EncerraVisitaDto encerraVisitaDto,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.encerraVisitaAmqpService.sendToConsumer(encerraVisitaDto);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
