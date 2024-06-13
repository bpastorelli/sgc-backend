package br.com.sgc.controllers;

import javax.validation.Valid;

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
import br.com.sgc.dto.AtualizaVinculoResidenciaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VinculoResidenciaDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Vinculo - Morador a Residência")
@RequestMapping("/sgc/vinculo")
@CrossOrigin(origins = "*")
class VinculoResidenciaController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<VinculoResidenciaDto, AtualizaVinculoResidenciaDto> vinculoAmqpService;
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para vincular um morador existente a uma residência existente.")
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VinculoResidenciaDto vinculoRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.vinculoAmqpService.sendToConsumerPost(vinculoRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
