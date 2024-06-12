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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.GETAcessoFuncionalidadeResponseDto;
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/access/acessoFuncionalidade")
@CrossOrigin(origins = "*")
public class AcessoFuncionalidadeController {
	
	@Autowired
	private ServicesAccess<CadastroAcessoFuncionalidadeDto, 
							AtualizaAcessoFuncionalidadeDto, 
							GETAcessoFuncionalidadeResponseDto, 
							AcessoFuncionalidadeFilter> service;
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarAcessosFuncionalidade(			
			AcessoFuncionalidadeFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		log.info("Buscando acessos por funcionalidade..");
		
		Page<GETAcessoFuncionalidadeResponseDto> acessos = this.service.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(acessos.getContent(), HttpStatus.OK) :
			new ResponseEntity<>(acessos, HttpStatus.OK);
		
	}
		
	@PutMapping(value = "/alterar")
	public ResponseEntity<?> atualizar(	
									@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
									@Valid @RequestBody List<AtualizaAcessoFuncionalidadeDto> acessoRequestBody) throws RegistroException {
		
		log.info("Aatualização de acessos: {}", acessoRequestBody.toString());
		Response<List<GETAcessoFuncionalidadeResponseDto>> response = new Response<List<GETAcessoFuncionalidadeResponseDto>>();
		
		response.setData(this.service.atualizaEmLote(acessoRequestBody, idUsuario));
		
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
		
}
