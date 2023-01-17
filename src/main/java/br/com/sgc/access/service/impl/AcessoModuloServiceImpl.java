package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.access.filter.AcessoModuloFilter;
import br.com.sgc.access.mapper.AcessoFuncionalidadeMapper;
import br.com.sgc.access.mapper.AcessoModuloMapper;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AcessoModuloServiceImpl implements ServicesAccess<CadastroAcessoModuloDto, AtualizaAcessoModuloDto, GETAcessoModuloResponseDto, AcessoModuloFilter> {
	
	@Autowired
	private AcessoModuloMapper mapperModulo;
	
	@Autowired
	private AcessoFuncionalidadeMapper mapperFuncionalidade;
	
	@Autowired
	private QueryRepository<AcessoModulo, AcessoModuloFilter> queryModulo;
	
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
			AcessoFuncionalidadeFilter filterAcessos = AcessoFuncionalidadeFilter.builder()
				.idUsuario(m.getIdUsuario())
				.idModulo(m.getIdModulo())
				.acesso(filter.isAcesso())
				.build();
			
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



}
