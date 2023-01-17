package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.GETAcessoFuncionalidadeResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.access.mapper.AcessoFuncionalidadeMapper;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AcessoFuncionalidadeServiceImpl implements ServicesAccess<CadastroAcessoFuncionalidadeDto, AtualizaAcessoFuncionalidadeDto, GETAcessoFuncionalidadeResponseDto, AcessoFuncionalidadeFilter> {

	@Autowired
	private AcessoFuncionalidadeMapper mapper;
	
	@Autowired
	private QueryRepository<AcessoFuncionalidade, AcessoFuncionalidadeFilter> query;
	
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

}
