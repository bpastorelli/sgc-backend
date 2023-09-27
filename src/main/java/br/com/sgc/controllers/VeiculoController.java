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
import br.com.sgc.dto.AtualizaVeiculoDto;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.filter.VeiculoFilter;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/veiculo")
@CrossOrigin(origins = "*")
class VeiculoController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<VeiculoDto, AtualizaVeiculoDto> cadastraVeiculoAmqpService;
	
	@Autowired
	private ServicesCore<GETVeiculoResponseDto, VeiculoFilter> veiculoService;
	
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VeiculoDto veiculoRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.cadastraVeiculoAmqpService.sendToConsumerPost(veiculoRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@PutMapping(value = "/amqp/alterar")
	public ResponseEntity<?> atualizarAMQP(
			@Valid @RequestBody AtualizaVeiculoDto veiculoRequestBody,
			@RequestParam(value = "id", defaultValue = "null") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		veiculoRequestBody.setId(id);
		ResponsePublisherDto response = this.cadastraVeiculoAmqpService.sendToConsumerPut(veiculoRequestBody, id);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVeiculosFiltro(
			VeiculoFilter filters,
			@PageableDefault(sort = "modelo", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException{
		
		
		log.info("Buscando veiculos...");
		
		Page<GETVeiculoResponseDto> veiculos = this.veiculoService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(veiculos.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(veiculos, HttpStatus.OK);
		
	}

}
