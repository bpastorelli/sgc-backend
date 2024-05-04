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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.AtualizaVisitanteDto;
import br.com.sgc.dto.GETVisitanteResponseDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.filter.VisitanteFilter;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/visitante")
@CrossOrigin(origins = "*")
class VisitanteController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<VisitanteDto, AtualizaVisitanteDto> visitanteAmqpService;
	
	@Autowired
	private ServicesCore<GETVisitanteResponseDto, VisitanteFilter> visitanteService;
	
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VisitanteDto visitanteRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitanteAmqpService.sendToConsumerPost(visitanteRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@PutMapping(value = "/amqp/alterar")
	public ResponseEntity<?> alterarAMQP( 
			@Valid @RequestBody AtualizaVisitanteDto visitanteRequestBody,
			@RequestParam(value = "id", defaultValue = "null") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		visitanteRequestBody.setId(id);
		ResponsePublisherDto response = this.visitanteAmqpService.sendToConsumerPut(visitanteRequestBody, id);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarMoradoresFiltro(
			VisitanteFilter filters,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		Page<GETVisitanteResponseDto> visitantes = this.visitanteService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(visitantes.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(visitantes, HttpStatus.OK);
		
	}

}
