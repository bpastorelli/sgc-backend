package br.com.sgc.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.EncerraVisitaDto;
import br.com.sgc.dto.GETVisitaResponseDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitaDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.filter.VisitaFilter;
import br.com.sgc.services.ServicesCore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Visitas")
@RequestMapping("/sgc/visita")
@CrossOrigin(origins = "*")
class VisitaController extends RegistroExceptionHandler {
	
	@Autowired
	private ServicesCore<GETVisitaResponseDto, VisitaFilter> visitaService;
	
	@Autowired
	private AmqpService<VisitaDto, EncerraVisitaDto> visitaAmqpService;
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para cadastro de visita.")
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VisitaDto visitaRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitaAmqpService.sendToConsumerPost(visitaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para encerrar uma visita.")
	@PutMapping(value = "/amqp/encerrar")
	public ResponseEntity<?> encerrarVisitaAMQP(@Valid @RequestBody EncerraVisitaDto encerraVisitaDto,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitaAmqpService.sendToConsumerPut(encerraVisitaDto, encerraVisitaDto.getId());
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Pesquisa visitas a partir dos filtros informados.")
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVisitasFiltro(
			VisitaFilter filters,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		log.info("Buscando visitas...");
		
		Page<GETVisitaResponseDto> visitas = this.visitaService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(visitas.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(visitas, HttpStatus.OK);
		
	}

}
