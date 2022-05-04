package br.com.sgc.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.amqp.service.AmqpService;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ResponsePublisherDto;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.response.Response;
import br.com.sgc.services.MoradorService;
	
@RestController
@RequestMapping("/sgc/morador")
@CrossOrigin(origins = "*")
public class MoradorController {
	
	private static final Logger log = LoggerFactory.getLogger(MoradorController.class);
	
	@Autowired
	private AmqpService<MoradorDto> moradorAmqpService;
	
	@Autowired
	private MoradorService moradorService;
	
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
	
	@PostMapping(value = "/novo")
	public ResponseEntity<?> cadastrarMoradores( 
			@Valid @RequestBody List<MoradorDto> moradoresRequestBody,
			BindingResult result) throws Exception{
		
		log.info("Cadastrando moradores em massa...");
		
		Response<List<MoradorDto>> response = this.moradorService.persistir(moradoresRequestBody);
		
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
	
	/**
	 * Busca um morador pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<CadastroMoradorResponseDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarMoradoresFiltro(
			MoradorFilter filters,
			@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		Page<MoradorDto> moradores = this.moradorService.buscarMorador(filters, paginacao);
		
		return new ResponseEntity<>(moradores.get(), HttpStatus.OK);
		
	}

	
}
