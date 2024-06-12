package br.com.sgc.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.sgc.dto.GETContribuicaoResponseDto;
import br.com.sgc.dto.GETHistoricoImportacaoResponseDto;
import br.com.sgc.enums.SituacaoEnum;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.ContribuicaoFilter;
import br.com.sgc.services.ContribuicaoService;
import br.com.sgc.services.ServicesCore;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Contribuições")
@RequestMapping("/sgc/contribuicao")
@CrossOrigin(origins = "*")
public class ContribuicaoController {
	
	@Autowired
	private ContribuicaoService service;
	
	@Autowired
	private ServicesCore<GETContribuicaoResponseDto, ContribuicaoFilter> serviceBusca;
	
	@PostMapping(value = "/import")
	public ResponseEntity<?> importacaoContribuicao(@RequestParam("file") MultipartFile file) throws RegistroException, IOException, InterruptedException, ExecutionException{
		
		try {
			
			return ResponseEntity.ok().body(this.service.processarContribuicoes(file));
			
		} catch (RegistroException e) {
			
			return ResponseEntity.badRequest().body(e.getErros());
			
		}
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVisitasFiltro(
			ContribuicaoFilter filters,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		log.info("Buscando lançamentos...");
		
		Page<GETContribuicaoResponseDto> visitas = this.serviceBusca.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(visitas.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(visitas, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/import/listar")
	public ResponseEntity<?> buscarHistoricoImportacaoFiltro(
			@RequestParam("situacao") List<SituacaoEnum> situacao) throws NoSuchAlgorithmException {
		
		List<GETHistoricoImportacaoResponseDto> historicoImportacao = this.service.buscarHistoricoImportacao(situacao);
		
		return new ResponseEntity<>(historicoImportacao, HttpStatus.OK);
		
	}

}
