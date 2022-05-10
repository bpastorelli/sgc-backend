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
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VeiculoDto;


@RestController
@RequestMapping("/sgc/veiculo")
@CrossOrigin(origins = "*")
class VeiculoController {
	
	private static final Logger log = LoggerFactory.getLogger(VeiculoController.class);
	
	@Autowired
	private AmqpService<VeiculoDto> veiculoAmqpService;
	
	public VeiculoController() {
		
	}
	
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VeiculoDto veiculoRequestBody,
											   BindingResult result ) throws Exception{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.veiculoAmqpService.sendToConsumer(veiculoRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
