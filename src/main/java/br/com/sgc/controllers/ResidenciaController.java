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
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.dto.ResponsePublisherDto;


@RestController
@RequestMapping("/sgc/residencia")
@CrossOrigin(origins = "*")
class ResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(ResidenciaController.class);
	
	@Autowired
	private AmqpService<ResidenciaDto> residenciaAmqpService;
	
	public ResidenciaController() {
		
	}
	
	@PostMapping(value = "/amqp/nova")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody ResidenciaDto residenciaRequestBody,
											   BindingResult result ) throws Exception{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.residenciaAmqpService.sendToConsumer(residenciaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
