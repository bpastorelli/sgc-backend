package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoModuloDto;
import br.com.sgc.access.dto.GETAcessoModuloResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.AcessoModulo;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.access.filter.AcessoModuloFilter;
import br.com.sgc.access.mapper.AcessoFuncionalidadeMapper;
import br.com.sgc.access.mapper.AcessoModuloMapper;
import br.com.sgc.access.repositories.AcessoModuloRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AcessoModuloServiceImpl implements ServicesAccess<CadastroAcessoModuloDto, AtualizaAcessoModuloDto, GETAcessoModuloResponseDto, AcessoModuloFilter> {
	
	@Autowired
	private Validators<CadastroAcessoModuloDto, AtualizaAcessoModuloDto> validar;
	
	@Autowired
	private AcessoModuloMapper mapperModulo;
	
	@Autowired
	private AcessoFuncionalidadeMapper mapperFuncionalidade;
	
	@Autowired
	private QueryRepository<AcessoModulo, AcessoModuloFilter> queryModulo;
	
	@Autowired
	private AcessoModuloRepository acessoModuloRepository;
	
	@Autowired
	private QueryRepository<AcessoFuncionalidade, AcessoFuncionalidadeFilter> queryFuncionalidade;
	
	@Override
	public GETAcessoModuloResponseDto cadastra(CadastroAcessoModuloDto post) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETAcessoModuloResponseDto> cadastraEmLote(List<CadastroAcessoModuloDto> post)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GETAcessoModuloResponseDto atualiza(AtualizaAcessoModuloDto put, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<GETAcessoModuloResponseDto> buscaPaginado(AcessoModuloFilter filter, Pageable pageable)
			throws RegistroException {

		log.info("Buscando acesso módulo(s)...");
		
		Response<List<GETAcessoModuloResponseDto>> response = new Response<List<GETAcessoModuloResponseDto>>(); 
		
		List<GETAcessoModuloResponseDto> listAcessos = new ArrayList<GETAcessoModuloResponseDto>();
		
		listAcessos.addAll(this.mapperModulo.listAcessoModuloToListGETAcessoModuloResponseDto(this.queryModulo.query(filter, pageable)));
		
		
		//Filtra as funcionalidades
		listAcessos.forEach(m -> {

			List<Long> modulos = new ArrayList<Long>();
			modulos.add(m.getIdModulo());
			AcessoFuncionalidadeFilter filterAcessos = new AcessoFuncionalidadeFilter();
			filterAcessos.setIdUsuario(m.getIdUsuario());
			filterAcessos.setIdModulo(modulos);
			filterAcessos.setAcesso(filter.isAcesso());
			filterAcessos.setContent(filter.isContent());
			
			m.setFuncionalidades(this.mapperFuncionalidade.listAcessoFuncionalidadeToListGETAcessoFuncionalidadeResponseDto(this.queryFuncionalidade.query(filterAcessos, pageable)));
		});
		
		response.setData(listAcessos);
		return new PageImpl<>(response.getData(), pageable, this.queryModulo.totalRegistros(filter));
		
	}

	@Override
	public GETAcessoModuloResponseDto busca(AcessoModuloFilter filter, Pageable pageable) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETAcessoModuloResponseDto> atualizaEmLote(List<AtualizaAcessoModuloDto> put, Long id)
			throws RegistroException {
		
		put.forEach(p -> {
			p.setIdUsuario(id);
		});
		
		this.validar.validarPut(put);
		
		return this.mapperModulo.listAcessoModuloToListGETAcessoModuloResponseDto(
				this.acessoModuloRepository.saveAll(this.convertToListAcessoModulo(put)));
		
	}
	
	private List<AcessoModulo> convertToListAcessoModulo(List<AtualizaAcessoModuloDto> dto){
		
		List<AcessoModulo> acessos = new ArrayList<AcessoModulo>();
		
		dto.forEach(a -> {
			
			Optional<AcessoModulo> acessoOld = this.acessoModuloRepository.findByIdUsuarioAndModuloId(a.getIdUsuario(), a.getIdModulo());
			
			if(acessoOld.isPresent()) {
				acessoOld.get().setAcesso(a.isAcesso());
			}else {
				AcessoModulo acesso = AcessoModulo.builder()
						.id(acessoOld.isPresent() ? acessoOld.get().getId() : null)
						.idUsuario(a.getIdUsuario())
						.modulo(Modulo.builder().id(a.getIdModulo()).build())
						.acesso(a.isAcesso())
						.build();
				acessos.add(acesso);	
			}
			
		});
		
		return acessos;
		
	}



}
