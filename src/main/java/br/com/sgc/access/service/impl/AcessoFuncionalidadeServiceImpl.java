package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.GETAcessoFuncionalidadeResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.Funcionalidade;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.access.mapper.AcessoFuncionalidadeMapper;
import br.com.sgc.access.repositories.AcessoFuncionalidadeRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AcessoFuncionalidadeServiceImpl implements ServicesAccess<CadastroAcessoFuncionalidadeDto, AtualizaAcessoFuncionalidadeDto, GETAcessoFuncionalidadeResponseDto, AcessoFuncionalidadeFilter> {

	@Autowired
	private AcessoFuncionalidadeMapper mapper;
	
	@Autowired
	private AcessoFuncionalidadeRepository acessoFuncionalidadeRepository;
	
	@Autowired
	private QueryRepository<AcessoFuncionalidade, AcessoFuncionalidadeFilter> query;
	
	@Autowired
	private Validators<CadastroAcessoFuncionalidadeDto, AtualizaAcessoFuncionalidadeDto> validar;
	
	@Override
	public GETAcessoFuncionalidadeResponseDto cadastra(CadastroAcessoFuncionalidadeDto post) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GETAcessoFuncionalidadeResponseDto> cadastraEmLote(List<CadastroAcessoFuncionalidadeDto> post)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GETAcessoFuncionalidadeResponseDto atualiza(AtualizaAcessoFuncionalidadeDto put, Long id)
			throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<GETAcessoFuncionalidadeResponseDto> buscaPaginado(AcessoFuncionalidadeFilter filter, Pageable pageable)
			throws RegistroException {

		log.info("Buscando acesso funcionalidades...");
		
		Response<List<GETAcessoFuncionalidadeResponseDto>> response = new Response<List<GETAcessoFuncionalidadeResponseDto>>(); 
		
		List<GETAcessoFuncionalidadeResponseDto> listAcessos = new ArrayList<GETAcessoFuncionalidadeResponseDto>();
		
		listAcessos.addAll(this.mapper.listAcessoFuncionalidadeToListGETAcessoFuncionalidadeResponseDto(this.query.query(filter, pageable)));
		
		response.setData(listAcessos);
		return new PageImpl<>(response.getData(), pageable, this.query.totalRegistros(filter));
		
	}

	@Override
	public GETAcessoFuncionalidadeResponseDto busca(AcessoFuncionalidadeFilter filter, Pageable pageable)
			throws RegistroException {
		
		this.query.query(filter);
		
		return null;
	}

	@Override
	public List<GETAcessoFuncionalidadeResponseDto> atualizaEmLote(List<AtualizaAcessoFuncionalidadeDto> put, Long id)
			throws RegistroException {

		put.forEach(p -> {
			p.setIdUsuario(id);
		});
		
		this.validar.validarPut(put);
		
		return this.mapper.listAcessoFuncionalidadeToListGETAcessoFuncionalidadeResponseDto(
				this.acessoFuncionalidadeRepository.saveAll(this.convertToListAcessoFuncionalidade(put)));
		
	}
	
	private List<AcessoFuncionalidade> convertToListAcessoFuncionalidade(List<AtualizaAcessoFuncionalidadeDto> dto){
		
		List<AcessoFuncionalidade> acessos = new ArrayList<AcessoFuncionalidade>();
		
		dto.forEach(a -> {
			
			Optional<AcessoFuncionalidade> acessoOld = this.acessoFuncionalidadeRepository.findByIdUsuarioAndFuncionalidadeIdAndModuloId(a.getIdUsuario(), a.getIdFuncionalidade(), a.getIdModulo());
			
			if(acessoOld.isPresent()) {
				acessoOld.get().setAcesso(a.isAcesso());
				acessoOld.get().setInclusao(a.isInclusao());
				acessoOld.get().setAlteracao(a.isAlteracao());
				acessoOld.get().setExclusao(a.isExclusao());
				
				if(!acessos.contains(acessoOld.get()))
					acessos.add(acessoOld.get());
			}else {
				AcessoFuncionalidade acesso = AcessoFuncionalidade.builder()
						.id(acessoOld.isPresent() ? acessoOld.get().getId() : null)
						.idUsuario(a.getIdUsuario())
						.funcionalidade(Funcionalidade.builder().id(a.getIdFuncionalidade()).build())
						.modulo(Modulo.builder().id(a.getIdModulo()).build())
						.acesso(a.isAcesso())
						.inclusao(a.isInclusao())
						.alteracao(a.isAlteracao())
						.exclusao(a.isExclusao())
						.build();
				if(!acessos.contains(acesso))
					acessos.add(acesso);	
			}
			
		});
		
		return acessos;
		
	}

}
