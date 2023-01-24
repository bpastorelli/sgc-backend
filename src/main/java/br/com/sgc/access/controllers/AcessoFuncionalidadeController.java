package br.com.sgc.access.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.access.dto.AcessoFuncionalidadeUsuarioResponseDto;
import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadePorModuloDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.GETAcessoFuncionalidadeResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.Funcionalidade;
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
	
	public AcessoFuncionalidadeController() {
		
	}
	
	@PostMapping(value = "/incluir/idUsuario/{idUsuario}/idModulo/{idModulo}")
	public ResponseEntity<?> cadastrar(
									@PathVariable("idUsuario") Long idUsuario,
									@PathVariable("idModulo") Long idModulo,
									@Valid @RequestBody List<CadastroAcessoFuncionalidadeDto> cadastroAcessoDto, 
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de acessos: {}", cadastroAcessoDto.toString());
		Response<List<AcessoFuncionalidade>> response = new Response<List<AcessoFuncionalidade>>();
		
		/*cadastroAcessoDto.forEach(c -> {
			c.setIdUsuario(idUsuario);
			c.setIdModulo(idModulo);
		});
		
		List<AcessoFuncionalidade> acessos = validarDadosPost(cadastroAcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		acessos = this.acessoFuncionalidadeService.persistir(acessos);
		
		response.setData(acessos);*/
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarAcessosFuncionalidade(			
			AcessoFuncionalidadeFilter filters,
			@PageableDefault(sort = "descricao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, RegistroException {
		
		log.info("Buscando acessos por funcionalidade..");
		
		Page<GETAcessoFuncionalidadeResponseDto> acessos = this.service.buscaPaginado(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(acessos.getContent(), HttpStatus.OK) :
			new ResponseEntity<>(acessos, HttpStatus.OK);
		
	}
		
	@PutMapping(value = "/idUsuario/{idUsuario}/idModulo/{idModulo}")
	public ResponseEntity<?> atualizar(	
									@PathVariable("idUsuario") Long idUsuario,
									@PathVariable("idModulo") Long idModulo,
									@Valid @RequestBody List<AtualizaAcessoFuncionalidadeDto> acessoRequestBody,
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualização de acessos: {}", acessoRequestBody.toString());
		Response<List<AcessoFuncionalidade>> response = new Response<List<AcessoFuncionalidade>>();
		
		/*List<AcessoFuncionalidade> acessos = this.acessoFuncionalidadeService.buscarPorUsuarioIdAndModuloId(idUsuario, idModulo);

		acessoRequestBody.forEach(p -> {
			p.setIdModulo(idModulo);
		});
		
		acessos = validarDadosPut(acessoRequestBody, acessos, idUsuario, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
			
		acessos = this.acessoFuncionalidadeService.persistir(acessos);
		
		response.setData(acessos);*/
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	public List<AcessoFuncionalidade> validarDadosPost(List<CadastroAcessoFuncionalidadeDto> listDto, BindingResult result) {
		
		List<AcessoFuncionalidade> listAcesso = new ArrayList<AcessoFuncionalidade>();
		
		/*listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(d.getIdUsuario()).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.funcionalidadeService.buscarPorIdAndIdModuloAndPosicao(d.getIdFuncionalidade(), d.getIdModulo(), (long)1).isPresent()) {
				result.addError(new ObjectError("funcionalidade", "A funcionalidade informada não pertence ao módulo"));
			}
			
			if(!this.moduloService.buscarPorId(d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("modulo", "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
			if(!this.funcionalidadeService.buscarPorId(d.getIdFuncionalidade()).isPresent()) {
				result.addError(new ObjectError("funcionalidade", "Funcionalidade inexistente para o código " + d.getIdFuncionalidade()));
			}
			
			if(this.acessoFuncionalidadeService.buscarPorIdUsuarioAndIdModuloAndIdFuncionalidade(d.getIdUsuario(), d.getIdModulo(), d.getIdFuncionalidade()).isPresent()) {
				result.addError(new ObjectError("acesso", "Funcionalidade e módulo já existente para este usuário"));
			}
			
			if(!result.hasErrors()) {
				AcessoFuncionalidade acesso = new AcessoFuncionalidade();
				acesso.setIdUsuario(d.getIdUsuario());	
				acesso.setIdModulo(d.getIdModulo());
				acesso.setIdFuncionalidade(d.getIdFuncionalidade());
				acesso.setAcesso(d.isAcesso());
				listAcesso.add(acesso);
			}
			
		});*/
		
		return listAcesso;
		
	}
	
	public List<AcessoFuncionalidade> validarDadosPut(List<AtualizaAcessoFuncionalidadeDto> listDto, List<AcessoFuncionalidade> listAcessos, Long idUsuario, BindingResult result) {
		
		List<AcessoFuncionalidade> listAcessosPut = new ArrayList<AcessoFuncionalidade>();
		
		/*listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(idUsuario).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + idUsuario));
			}
			
			if(!this.moduloService.buscarPorId(d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
			if(!this.funcionalidadeService.buscarPorId(d.getIdFuncionalidade()).isPresent()) {
				result.addError(new ObjectError("funcionalidade", "Funcionalidade inexistente para o código " + d.getIdFuncionalidade()));
			}
			
		});
		
		//Se não houverem erros monta a lista de persistencia
		if(!result.hasErrors()) {
			listAcessosPut = this.atualizaAcesso(listAcessos, listDto, idUsuario);
		}*/
		
		return listAcessosPut;
		
	}
	
	public List<AcessoFuncionalidade> validarDadosPut(List<AtualizaAcessoFuncionalidadePorModuloDto> listDto, List<AcessoFuncionalidade> listAcessos, Long idUsuario, Long idModulo, BindingResult result) {
		
		List<AcessoFuncionalidade> listAcessosPut = new ArrayList<AcessoFuncionalidade>();
		
		/*listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(idUsuario).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + idUsuario));
			}
			
			if(!this.moduloService.buscarPorId(idModulo).isPresent()) {
				result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + idModulo));
			}
			
			if(!this.funcionalidadeService.buscarPorId(d.getIdFuncionalidade()).isPresent()) {
				result.addError(new ObjectError("funcionalidade", "Funcionalidade inexistente para o código " + d.getIdFuncionalidade()));
			}
			
		});
		
		//Se não houverem erros monta a lista de persistencia
		if(!result.hasErrors()) {
			listAcessosPut = this.atualizaAcesso(listAcessos, listDto, idUsuario, idModulo);
		}*/
		
		return listAcessosPut;
		
	}
	
	public List<AcessoFuncionalidade> atualizaAcesso(List<AcessoFuncionalidade> acessos, List<AtualizaAcessoFuncionalidadeDto> acessosDto, Long idUsuario) {
		
		List<AcessoFuncionalidade> listAcessos = new ArrayList<AcessoFuncionalidade>();
		
		/*acessosDto.forEach(a -> {
			
			List<AcessoFuncionalidade> result = acessos.stream()
						.filter(item -> item.getIdFuncionalidade().equals(a.getIdFuncionalidade()))
						.collect(Collectors.toList());
			
			AcessoFuncionalidade acesso = new AcessoFuncionalidade();
			
			if(result.size() > 0) {	
			
				acesso.setId(result.get(0).getId());
				acesso.setIdUsuario(result.get(0).getIdUsuario());
				acesso.setIdFuncionalidade(result.get(0).getIdFuncionalidade());
				acesso.setIdModulo(result.get(0).getIdModulo());
				acesso.setDataCadastro(result.get(0).getDataCadastro());
				acesso.setPosicao(result.get(0).getPosicao());
				acesso.setAcesso(a.isAcesso());
				
				listAcessos.add(acesso);
				
			}else {
				
				acesso.setIdUsuario(idUsuario);
				acesso.setIdFuncionalidade(a.getIdFuncionalidade());
				acesso.setIdModulo(a.getIdModulo());
				acesso.setAcesso(a.isAcesso());
				acesso.setDataCadastro(new Date());
				
				listAcessos.add(acesso);
				
			}			
			
		});*/
		
		return listAcessos;
		
	}
	
	public List<AcessoFuncionalidade> atualizaAcesso(List<AcessoFuncionalidade> acessos, List<AtualizaAcessoFuncionalidadePorModuloDto> acessosDto, Long idUsuario, Long idModulo) {
		
		List<AcessoFuncionalidade> listAcessos = new ArrayList<AcessoFuncionalidade>();
		
		/*acessosDto.forEach(a -> {
			
			AcessoFuncionalidade acesso = new AcessoFuncionalidade();
			
			List<AcessoFuncionalidade> result = acessos.stream()
						.filter(item -> item.getIdFuncionalidade().equals(a.getIdFuncionalidade()))
						.collect(Collectors.toList());
			
			if(result.size() > 0) {	
			
				acesso.setId(result.get(0).getId());
				acesso.setIdUsuario(result.get(0).getIdUsuario());
				acesso.setIdFuncionalidade(result.get(0).getIdFuncionalidade());
				acesso.setIdModulo(result.get(0).getIdModulo());
				acesso.setDataCadastro(result.get(0).getDataCadastro());
				acesso.setPosicao(result.get(0).getPosicao());
				acesso.setAcesso(a.isAcesso());
				
				listAcessos.add(acesso);
				
			}else {
				
				acesso.setIdUsuario(idUsuario);
				acesso.setIdFuncionalidade(a.getIdFuncionalidade());
				acesso.setIdModulo(idModulo);
				acesso.setAcesso(a.isAcesso());
				acesso.setDataCadastro(new Date());
				
				listAcessos.add(acesso);
				
			}			
			
		});*/
		
		return listAcessos;
		
	}
	
	public List<GETAcessoFuncionalidadeResponseDto> montaResponse(List<AcessoFuncionalidade> acessosFuncionalidade){
		
		List<GETAcessoFuncionalidadeResponseDto> list = new ArrayList<GETAcessoFuncionalidadeResponseDto>();
		
		/*acessosFuncionalidade.forEach(m -> {
			
			Optional<Funcionalidade> funcionalidade = this.funcionalidadeService.buscarPorId(m.getIdFuncionalidade());
			
			GETAcessoFuncionalidadeResponseDto aFuncionalidade = new GETAcessoFuncionalidadeResponseDto();
			aFuncionalidade.setId(m.getId());
			aFuncionalidade.setIdFuncionalidade(funcionalidade.get().getId());
			aFuncionalidade.setIdModulo(m.getIdModulo());
			aFuncionalidade.setDescricao(funcionalidade.get().getDescricao());
			aFuncionalidade.setPath(funcionalidade.get().getPathFuncionalidade());
			aFuncionalidade.setAcesso(m.isAcesso());			
			
			list.add(aFuncionalidade);
			
		});
		
		Collections.sort(list);*/
		
		return list;
		
	}
	
	public List<AcessoFuncionalidadeUsuarioResponseDto> montaResponse(Page<Funcionalidade> funcionalidades, List<AcessoFuncionalidade> acessos){
		
		List<AcessoFuncionalidadeUsuarioResponseDto> list = new ArrayList<AcessoFuncionalidadeUsuarioResponseDto>();
		
		//Todas as funcionalidades
		/*funcionalidades.forEach(f -> {
			
			List<AcessoFuncionalidade> af = new ArrayList<AcessoFuncionalidade>(); 
			af = acessos.stream().filter(p -> p.getIdFuncionalidade().equals(f.getId())).collect(Collectors.toList());
			
			AcessoFuncionalidadeUsuarioResponseDto funcionalidade = new AcessoFuncionalidadeUsuarioResponseDto();
			funcionalidade.setIdModulo(f.getIdModulo());
			funcionalidade.setIdFuncionalidade(f.getId());
			funcionalidade.setFuncionalidade(f.getDescricao());
			funcionalidade.setAcesso(af.size() > 0 ? af.get(0).isAcesso() : false);
			
			list.add(funcionalidade);
			
		});
		
		Collections.sort(list);*/
		
		return list;
		
	}
	
}
