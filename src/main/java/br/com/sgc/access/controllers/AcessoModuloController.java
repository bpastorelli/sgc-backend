package br.com.sgc.access.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

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

import br.com.sgc.access.dto.AtualizaAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoModuloResponseDto;
import br.com.sgc.access.dto.GETAcessoModuloResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.AcessoModulo;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.AcessoModuloFilter;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sgc/access/acessoModulo")
@CrossOrigin(origins = "*")
public class AcessoModuloController {
	
	@Autowired
	private ServicesAccess<CadastroAcessoModuloDto, 
								AtualizaAcessoModuloDto, 
								GETAcessoModuloResponseDto, 
								AcessoModuloFilter> service;
	
	
	public AcessoModuloController() {
		
	}
	
	@PostMapping(value = "/processo")
	public ResponseEntity<?> cadastrarProcesso(@Valid @RequestBody List<CadastroAcessoModuloDto> cadastroAcessoDto, 
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de acessos: {}", cadastroAcessoDto.toString());
		Response<List<CadastroAcessoModuloResponseDto>> response = new Response<List<CadastroAcessoModuloResponseDto>>();
		
		/*List<AcessoModulo> acessosModulo = validarDadosPost(cadastroAcessoDto, result);
		List<AcessoFuncionalidade> acessosFuncionalidade = validarDadosFuncionalidadePost(cadastroAcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		acessosModulo = this.acessoModuloService.persistir(acessosModulo);
		acessosFuncionalidade = this.acessoFuncionalidadeService.persistir(acessosFuncionalidade);
		
		List<CadastroAcessoModuloResponseDto> listResponse = this.montaResponse(acessosModulo, acessosFuncionalidade);
		
		response.setData(listResponse);*/
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}

	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(@Valid @RequestBody List<CadastroAcessoModuloDto> cadastroAcessoDto, 
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de acessos: {}", cadastroAcessoDto.toString());
		Response<List<GETAcessoModuloResponseDto>> response = new Response<List<GETAcessoModuloResponseDto>>();
		
		/*List<AcessoModulo> acessosModulo = validarDadosPost(cadastroAcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		acessosModulo = this.acessoModuloService.persistir(acessosModulo);		
		List<AcessoModuloResponseDto> listResponse = this.montaResponse(acessosModulo);
		
		response.setData(listResponse);*/
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}

	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarAcessos(			
			AcessoModuloFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		log.info("Buscando acessos..");
		
		Page<GETAcessoModuloResponseDto> acessos = this.service.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(acessos.getContent(), HttpStatus.OK) :
			new ResponseEntity<>(acessos, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/buscaModulos")
	public ResponseEntity<?> buscarAcessosPorUsuario(			
			@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
			@RequestParam(value = "acesso", defaultValue = "true") boolean acesso) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos de módulos do usuário {} e acesso tipo {}", idUsuario, acesso);
		return null;
		
		/*List<AcessoModulo> modulos = new ArrayList<AcessoModulo>();
		
		if(idUsuario != 0 && idUsuario != null) {
			modulos =  acessoModuloService.buscarPorUsuarioIdAndAcesso(idUsuario, acesso);
		}
		
		if (modulos.size() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		List<AcessoModuloResponseDto> list = this.montaResponseModuloFuncionalidade(modulos, acessoFuncionalidadeService.buscarPorUsuarioIdAndAcesso(idUsuario, acesso));
		
		return ResponseEntity.status(HttpStatus.OK).body(list);*/
		
	}
	
	@GetMapping(value = "/busca")
	public ResponseEntity<?> buscarAcessosPorUsuarioDescricao(			
			@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
			@RequestParam(value = "idModulo", defaultValue = "0") Long idModulo) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos do usuário {} e módulo {}", idUsuario, idModulo);
		return null;
		
		/*Optional<AcessoModulo> acesso =  acessoModuloService.buscarPorIdUsuarioAndIdModulo(idUsuario, idModulo);
		
		if (!acesso.isPresent()) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		Optional<AcessoModuloResponseDto> response = this.montaResponse(acesso.get());*/
		
		//return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	@PutMapping(value = "/alterar")
	public ResponseEntity<?> atualizar(	
									@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
									@Valid @RequestBody List<AtualizaAcessoModuloDto> acessoRequestBody) throws RegistroException {
		
		log.info("Aatualização de acessos: {}", acessoRequestBody.toString());
		Response<List<GETAcessoModuloResponseDto>> response = new Response<List<GETAcessoModuloResponseDto>>();
		
		response.setData(this.service.atualizaEmLote(acessoRequestBody, idUsuario));
		
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtroPorUsuario")
	public ResponseEntity<?> buscarAcessosModuloUsuario(			
			@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
			@RequestParam(value = "posicao", defaultValue = "1") Long posicao,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "descricao") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos a funcionalidades por usuário e módulo...");
		return null;
		
		/*PageRequest pageRequest = PageRequest.of(pag, size, Direction.valueOf(dir), ord);
		Page<Modulo> modulos = null;
		List<AcessoModulo> acessos = null;
		
		if(idUsuario != 0 && idUsuario != null) {
			modulos = moduloService.buscarPorPosicao(posicao, pageRequest);
			acessos = acessoModuloService.buscarPorUsuarioId(idUsuario);
		}
		
		if (modulos.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		List<AcessoModuloResponseDto> response = this.montaResponse(modulos, acessos);*/
		
		//return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	public List<AcessoFuncionalidade> validarDadosFuncionalidadePost(List<CadastroAcessoModuloDto> listDto, BindingResult result) {
		return null;
		
		/*List<AcessoFuncionalidade> listAcesso = new ArrayList<AcessoFuncionalidade>();
		
		listDto.forEach(m -> {
			
			m.getFuncionalidades().forEach(f -> {
				
				if(!this.moradorService.buscarPorId(m.getIdUsuario()).isPresent()) {
					result.addError(new ObjectError("morador", "Usuário inexistente para o código " + f.getIdUsuario()));
				}
				
				if(!this.moduloService.buscarPorId(m.getIdModulo()).isPresent()) {
					result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + f.getIdModulo()));
				}
				
				if(!this.funcionalidadeService.buscarPorId(f.getIdFuncionalidade()).isPresent()) {
					result.addError(new ObjectError("funcionalidade", "Funcionalidade inexistente para o código " + f.getIdFuncionalidade()));
				}
				
				if(this.acessoFuncionalidadeService.buscarPorIdUsuarioAndIdModuloAndIdFuncionalidade(f.getIdUsuario(), f.getIdModulo(), f.getIdFuncionalidade()).isPresent()) {
					result.addError(new ObjectError("acesso", "Funcionalidade " + f.getIdFuncionalidade() + " e módulo já existente para este usuário"));
				}
				
				if(!result.hasErrors()) {
					AcessoFuncionalidade acesso = new AcessoFuncionalidade();
					acesso.setIdUsuario(m.getIdUsuario());	
					acesso.setIdModulo(m.getIdModulo());
					acesso.setIdFuncionalidade(f.getIdFuncionalidade());
					acesso.setAcesso(f.isAcesso());
					listAcesso.add(acesso);
				}
				
			});
			
		});
		
		return listAcesso;*/
		
	}
	
	public List<CadastroAcessoModuloResponseDto> montaResponse(List<AcessoModulo> acessosModulos, List<AcessoFuncionalidade> funcionalidades){
		return null;
		
		/*List<CadastroAcessoModuloResponseDto> listResponse = new ArrayList<CadastroAcessoModuloResponseDto>();
		
		CadastroAcessoModuloResponseDto dto = new CadastroAcessoModuloResponseDto();
		
		List<Modulo> modulos = new ArrayList<Modulo>();
		
		acessosModulos.forEach(m -> {
			modulos.add(m.getModulo());
		});	
		
		dto.setModulos(modulos);
		dto.setFuncionalidades(funcionalidades);
		
		listResponse.add(dto);
		
		return listResponse;*/
		
	}
	
	public Optional<GETAcessoModuloResponseDto> montaResponse(AcessoModulo acessoModulo){
		return null;
		
		/*AcessoModuloResponseDto response = new AcessoModuloResponseDto();
		
		response.setId(acessoModulo.getId());
		response.setIdModulo(acessoModulo.getModulo().getId());
		response.setDescricao(acessoModulo.getModulo().getDescricao());
		response.setAcesso(acessoModulo.isAcesso());
		
		return Optional.ofNullable(response);*/
	}
	
	public List<GETAcessoModuloResponseDto> montaResponseModuloFuncionalidade(List<AcessoModulo> acessosModulo, List<AcessoFuncionalidade> acessosFuncionalidades){
		return null;
		
		/*List<AcessoModuloResponseDto> list = new ArrayList<AcessoModuloResponseDto>();
		
		acessosModulo.forEach(m -> {
			
			Optional<Modulo> modulo = this.moduloService.buscarPorId(m.getModulo().getId());
			List<AcessoFuncionalidadeUsuarioResponseDto> listFunc = new ArrayList<AcessoFuncionalidadeUsuarioResponseDto>();
			
			AcessoModuloResponseDto aModulo = new AcessoModuloResponseDto();
			aModulo.setId(m.getId());
			aModulo.setIdModulo(modulo.get().getId());
			aModulo.setDescricao(modulo.get().getDescricao());
			aModulo.setPath(modulo.get().getPathModulo());
	
			List<AcessoFuncionalidade> listFuncFilter = new ArrayList<AcessoFuncionalidade>(); 
			listFuncFilter = acessosFuncionalidades.stream().filter(p -> p.getIdModulo()
																	.equals(m.getModulo().getId()))
																	.collect(Collectors.toList());	
			
			if(listFuncFilter.size() > 0) {
				listFuncFilter.forEach(f -> {
					Optional<Funcionalidade> funcionalidade = this.funcionalidadeService.buscarPorId(f.getIdFuncionalidade());
					AcessoFuncionalidadeUsuarioResponseDto func = new AcessoFuncionalidadeUsuarioResponseDto();
					func.setIdModulo(f.getIdModulo());
					func.setIdFuncionalidade(f.getIdFuncionalidade());
					func.setFuncionalidade(funcionalidade.get().getDescricao());
					func.setPath(funcionalidade.get().getPathFuncionalidade());
					func.setAcesso(f.isAcesso());
					listFunc.add(func);
				});				
			}
			
			aModulo.setFuncionalidades(listFunc);
			aModulo.setAcesso(m.isAcesso());			
			
			list.add(aModulo);
			
		});
		
		Collections.sort(list);
		
		return list;*/
		
	}
	
	public List<GETAcessoModuloResponseDto> montaResponse(List<AcessoModulo> acessosModulo){
		return null;
		
		/*List<AcessoModuloResponseDto> list = new ArrayList<AcessoModuloResponseDto>();
		
		acessosModulo.forEach(m -> {
			
			Optional<Modulo> modulo = this.moduloService.buscarPorId(m.getModulo().getId());
			List<AcessoFuncionalidadeUsuarioResponseDto> listFunc = new ArrayList<AcessoFuncionalidadeUsuarioResponseDto>();
			
			AcessoModuloResponseDto aModulo = new AcessoModuloResponseDto();
			aModulo.setId(m.getId());
			aModulo.setIdModulo(modulo.get().getId());
			aModulo.setDescricao(modulo.get().getDescricao());
			aModulo.setPath(modulo.get().getPathModulo());		
			aModulo.setFuncionalidades(listFunc);
			aModulo.setAcesso(m.isAcesso());			
			
			list.add(aModulo);
			
		});
		
		Collections.sort(list);
		
		return list;*/
		
	}
	
	public List<GETAcessoModuloResponseDto> montaResponse(Page<Modulo> modulos, List<AcessoModulo> acessos){
		return null;
		
		/*List<AcessoModuloResponseDto> list = new ArrayList<AcessoModuloResponseDto>();
		
		//Todas os módulos
		modulos.forEach(m -> {
			
			List<AcessoModulo> am = new ArrayList<AcessoModulo>(); 
			am = acessos.stream().filter(p -> p.getModulo().getId().equals(m.getId())).collect(Collectors.toList());
			
			AcessoModuloResponseDto modulo = new AcessoModuloResponseDto();
			modulo.setId(am.size() > 0 ? am.get(0).getId() : 0);
			modulo.setIdModulo(m.getId());
			modulo.setDescricao(m.getDescricao());
			modulo.setPath(m.getPathModulo());
			modulo.setAcesso(am.size() > 0 ? am.get(0).isAcesso() : false);
			
			list.add(modulo);
			
		});
		
		Collections.sort(list);
		
		return list;*/
		
	}
	
}
