package br.com.sgc.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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
import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.AtualizaProcessoCadastroDto;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.response.Response;
import br.com.sgc.services.MoradorService;
import lombok.extern.slf4j.Slf4j;
	
@Slf4j
@RestController
@RequestMapping("/sgc/morador")
@CrossOrigin(origins = "*")
public class MoradorController extends RegistroExceptionHandler {
	
	@Autowired
	private AmqpService<MoradorDto, AtualizaMoradorDto> moradorAmqpService;
	
	@Autowired
	private AmqpService<ProcessoCadastroDto, AtualizaProcessoCadastroDto> processoAmqpService;
	
	@Autowired
	private MoradorService<MoradorDto> moradorService;
	
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
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.moradorAmqpService.sendToConsumerPost(moradorRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	/**
	 * Envia um objeto tipo MoradorDto para o Consumer
	 * @param moradorRequestBody
	 * @param result 
	 * @return ResponsePublisher
	 * @throws Exception
	 */
	@PostMapping(value = "/amqp/processo")
	public ResponseEntity<?> processoCadastroAMQP( 
			@Valid @RequestBody ProcessoCadastroDto processoRequestBody,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.processoAmqpService.sendToConsumerPost(processoRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@PutMapping(value = "/amqp/alterar")
	public ResponseEntity<?> alterarAMQP( 
			@Valid @RequestBody AtualizaMoradorDto moradorRequestBody,
			@RequestParam(value = "id", defaultValue = "null") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		moradorRequestBody.setId(id);
		ResponsePublisherDto response = this.moradorAmqpService.sendToConsumerPut(moradorRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<?> cadastrarMoradores( 
			@Valid @RequestBody List<MoradorDto> moradoresRequestBody,
			BindingResult result) throws RegistroException{
		
		log.info("Cadastrando moradores em massa...");
		
		Response<List<GETMoradorResponseDto>> response = this.moradorService.persistir(moradoresRequestBody);
		
		return response.getErrors().size() > 0 ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	@GetMapping(value = "/amqp/ticket")
	public ResponseEntity<?> buscarPorTicket(
			@RequestParam(value = "ticket", defaultValue = "null") String ticket){
		
		Response<MoradorDto> response = this.moradorService.buscarPorGuide(ticket);	
		
		return response.getErrors().size() > 0 ?
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrors()) :
				ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarMoradoresFiltro(
			MoradorFilter filters,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		Page<GETMoradorResponseDto> moradores = this.moradorService.buscarMorador(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(moradores.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(moradores, HttpStatus.OK);
		
	}

	
}
