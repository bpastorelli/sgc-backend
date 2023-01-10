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
import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/residencia")
@CrossOrigin(origins = "*")
class ResidenciaController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<ResidenciaDto, AtualizaResidenciaDto> residenciaAmqpService;
	
	@Autowired
	private ServicesCore<GETResidenciaResponseDto, ResidenciaFilter> residenciaService;
	
	public ResidenciaController() {
		
	}
	
	@PostMapping(value = "/amqp/nova")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody ResidenciaDto residenciaRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.residenciaAmqpService.sendToConsumerPost(residenciaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarResidenciasFiltro(
			ResidenciaFilter filters,
			@PageableDefault(sort = "endereco", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		Page<GETResidenciaResponseDto> moradores = this.residenciaService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(moradores.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(moradores, HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/amqp/alterar")
	public ResponseEntity<?> alterarAMQP( 
			@Valid @RequestBody AtualizaResidenciaDto residenciaRequestBody,
			@RequestParam(value = "id", defaultValue = "null") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		residenciaRequestBody.setId(id);
		ResponsePublisherDto response = this.residenciaAmqpService.sendToConsumerPut(residenciaRequestBody, id);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
