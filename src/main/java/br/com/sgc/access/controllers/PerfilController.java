package br.com.sgc.access.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.AtualizaAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoModuloDto;
import br.com.sgc.access.dto.GETPerfilFuncionalidadeResponseDto;
import br.com.sgc.access.dto.GETPerfilModuloResponseDto;
import br.com.sgc.access.filter.PerfilFuncionalidadeFilter;
import br.com.sgc.access.filter.PerfilModuloFilter;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/access/perfil")
@CrossOrigin(origins = "*")
public class PerfilController {
	
	@Autowired
	private ServicesAccess<List<CadastroAcessoModuloDto>, 
						   List<AtualizaAcessoModuloDto>, 
						   GETPerfilModuloResponseDto, 
						   PerfilModuloFilter> serviceModulo;
	
	@Autowired
	private ServicesAccess<List<CadastroAcessoFuncionalidadeDto>, 
						   List<AtualizaAcessoFuncionalidadeDto>, 
						   GETPerfilFuncionalidadeResponseDto, 
						   PerfilFuncionalidadeFilter> serviceFuncionalidade;
	
	@GetMapping(value = "/modulo/filtro")
	public ResponseEntity<?> buscarAcessosModulos(			
			PerfilModuloFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		log.info("Buscando acessos..");
		
		Page<GETPerfilModuloResponseDto> acessos = this.serviceModulo.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(acessos.getContent(), HttpStatus.OK) :
			new ResponseEntity<>(acessos, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/funcionalidade/filtro")
	public ResponseEntity<?> buscarAcessosFuncionalidades(			
			PerfilFuncionalidadeFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		log.info("Buscando acessos..");
		
		Page<GETPerfilFuncionalidadeResponseDto> acessos = this.serviceFuncionalidade.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(acessos.getContent(), HttpStatus.OK) :
			new ResponseEntity<>(acessos, HttpStatus.OK);
		
	}
	
	

}
