package br.com.sgc.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
	
@RestController
@RequestMapping("/sgc/morador")
@CrossOrigin(origins = "*")
public class MoradorController {
	
	private static final Logger log = LoggerFactory.getLogger(MoradorController.class);
	
	@Autowired
	private AmqpService<MoradorDto> moradorAmqpService;
	
	
	public MoradorController() {
		
	}
		
	/**
	 * Envia um objeto tipo MoradorDto para o Consumer
	 * @param moradorRequestBody
	 * @param result 
	 * @return ResponsePublisher
	 * @throws Exception
	 */
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP( 
			@Valid @RequestBody MoradorDto moradorRequestBody,
			BindingResult result) throws Exception{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.moradorAmqpService.sendToConsumer(moradorRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
}
