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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.access.dto.AtualizaFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroFuncionalidadeDto;
import br.com.sgc.access.dto.GETFuncionalidadeResponseDto;
import br.com.sgc.access.filter.FuncionalidadeFilter;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/sgc/access/funcionalidade")
@CrossOrigin(origins = "*")
public class FuncionalidadeController extends RegistroExceptionHandler {
	
	@Autowired
	private ServicesAccess<CadastroFuncionalidadeDto, AtualizaFuncionalidadeDto, GETFuncionalidadeResponseDto, FuncionalidadeFilter> service;
	
	public FuncionalidadeController() {
		
		
	}
	
	@PostMapping(value = "/incluirEmMassa")
	public ResponseEntity<?> cadastrarEmMassa(
			@Valid @RequestBody List<CadastroFuncionalidadeDto> requestBody) throws RegistroException{
		
		log.info("Cadastro de funcionalidades: {}", requestBody.toString());
		Response<List<GETFuncionalidadeResponseDto>> response = new Response<List<GETFuncionalidadeResponseDto>>();
		
		response.setData(this.service.cadastraEmLote(requestBody));
		
		return response.getErrors().size() == 0 ? 
				ResponseEntity.status(HttpStatus.OK).body(response.getData()) :
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrors());
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(
			@Valid @RequestBody CadastroFuncionalidadeDto requestBody) throws RegistroException{
		
		log.info("Cadastro de funcionalidades: {}", requestBody.toString());
		Response<GETFuncionalidadeResponseDto> response = new Response<GETFuncionalidadeResponseDto>();
		
		response.setData(this.service.cadastra(requestBody));
		
		return response.getErrors().size() == 0 ? 
				ResponseEntity.status(HttpStatus.OK).body(response.getData()) :
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrors()); 
		
	}
	
	@PutMapping(value = "/alterar")
	public ResponseEntity<?> atualizar(
			@RequestParam(value = "id", defaultValue = "null") Long id,
			@Valid @RequestBody AtualizaFuncionalidadeDto requestBody) throws NoSuchAlgorithmException, RegistroException{
		
		log.info("Atualização de funcionalidade: {}", requestBody.toString());
		Response<GETFuncionalidadeResponseDto> response = new Response<GETFuncionalidadeResponseDto>();
		
		response.setData(this.service.atualiza(requestBody, id));
		
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarModuloFiltro(
			FuncionalidadeFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		Page<GETFuncionalidadeResponseDto> modulos = this.service.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(modulos.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(modulos, HttpStatus.OK);
		
	}

}
