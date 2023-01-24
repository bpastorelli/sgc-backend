package br.com.sgc.access.controllers;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.access.dto.AtualizaModuloDto;
import br.com.sgc.access.dto.CadastroModuloDto;
import br.com.sgc.access.dto.GETModuloResponseDto;
import br.com.sgc.access.filter.ModuloFilter;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/sgc/access/modulo")
@CrossOrigin(origins = "*")
public class ModuloController extends RegistroExceptionHandler {
	
	@Autowired
	private ServicesAccess<CadastroModuloDto, AtualizaModuloDto, GETModuloResponseDto, ModuloFilter> service;
	
	public ModuloController() {
		
		
	}
	
	@PostMapping(value = "/incluirEmMassa")
	public ResponseEntity<?> cadastrarEmMassa(
			@Valid @RequestBody List<CadastroModuloDto> requestBody, 
			BindingResult result) throws RegistroException{
		
		log.info("Cadastro de módulos: {}", requestBody.toString());
		Response<List<GETModuloResponseDto>> response = new Response<List<GETModuloResponseDto>>();
		
		response.setData(this.service.cadastraEmLote(requestBody));
		
		return response.getErrors().size() == 0 ? 
				ResponseEntity.status(HttpStatus.OK).body(response.getData()) :
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrors());
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(
			@Valid @RequestBody CadastroModuloDto requestBody, 
			BindingResult result) throws RegistroException{
		
		log.info("Cadastro de módulos: {}", requestBody.toString());
		Response<GETModuloResponseDto> response = new Response<GETModuloResponseDto>();
		
		response.setData(this.service.cadastra(requestBody));
		
		return response.getErrors().size() == 0 ? 
				ResponseEntity.status(HttpStatus.OK).body(response.getData()) :
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrors()); 
		
	}
	
	@PutMapping(value = "/id/{id}")
	public ResponseEntity<?> atualizar(
			@PathVariable("id") Long id,
			@Valid @RequestBody AtualizaModuloDto requestBody, 
			BindingResult result) throws NoSuchAlgorithmException, RegistroException{
		
		log.info("Atualização de módulo: {}", requestBody.toString());
		Response<GETModuloResponseDto> response = new Response<GETModuloResponseDto>();
		
		response.setData(this.service.atualiza(requestBody, id));
		
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarModuloFiltro(
			ModuloFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		Page<GETModuloResponseDto> modulos = this.service.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(modulos.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(modulos, HttpStatus.OK);
		
	}

}
