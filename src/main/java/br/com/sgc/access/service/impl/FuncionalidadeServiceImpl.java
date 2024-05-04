package br.com.sgc.access.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.access.dto.AtualizaFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroFuncionalidadeDto;
import br.com.sgc.access.dto.GETFuncionalidadeResponseDto;
import br.com.sgc.access.entities.Funcionalidade;
import br.com.sgc.access.filter.FuncionalidadeFilter;
import br.com.sgc.access.mapper.FuncionalidadeMapper;
import br.com.sgc.access.repositories.FuncionalidadeRepository;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesAccess;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FuncionalidadeServiceImpl implements ServicesAccess<CadastroFuncionalidadeDto, AtualizaFuncionalidadeDto, GETFuncionalidadeResponseDto, FuncionalidadeFilter> {

	@Autowired
	private FuncionalidadeMapper mapper;
	
	@Autowired
	private QueryRepository<Funcionalidade, FuncionalidadeFilter> query;
	
	@Autowired
	private Validators<CadastroFuncionalidadeDto, AtualizaFuncionalidadeDto> validar;
	
	@Autowired
	private FuncionalidadeRepository funcionalidadeRepository;
	
	@Override
	public GETFuncionalidadeResponseDto cadastra(CadastroFuncionalidadeDto post) throws RegistroException {
		
		log.info("Cadastrando funcionalidade...");
		
		Funcionalidade funcionalidade = this.mapper.cadastroFuncionalidadeDtoToFuncionalidade(post);
		
		//Validação
		this.validar.validarPost(post);
		
		GETFuncionalidadeResponseDto response = this.mapper.funcionalidadeToGETFuncionalidadeResponseDto(this.funcionalidadeRepository.save(funcionalidade));
		
		return response;
	}
	
	@Override
	public List<GETFuncionalidadeResponseDto> cadastraEmLote(List<CadastroFuncionalidadeDto> post) throws RegistroException {

		log.info("Cadastrando funcionalidades...");
		
		List<GETFuncionalidadeResponseDto> response = new ArrayList<GETFuncionalidadeResponseDto>();

		this.validar.validarPost(post);	
		
		this.funcionalidadeRepository.saveAll(this.mapper.listCadastroFuncionalidadeDtoToListFuncionalidade(post)).forEach(m -> {
			response.add(this.mapper.funcionalidadeToGETFuncionalidadeResponseDto(m));
		});
		
		return response;
		
	}

	@Override
	public GETFuncionalidadeResponseDto atualiza(AtualizaFuncionalidadeDto put, Long id) throws RegistroException {
		
		log.info("Atualizando funcionalidade...");		
		
		//Validação
		this.validar.validarPut(put, id);
		
		Funcionalidade funcionalidade = this.funcionalidadeRepository.findById(id).get();
		
		funcionalidade.setDescricao(put.getDescricao());
		funcionalidade.setFuncao(put.getFuncao());
		funcionalidade.setPathFuncionalidade(put.getPathFuncionalidade().toLowerCase());
		funcionalidade.setPosicao(put.getPosicao());
		
		GETFuncionalidadeResponseDto response = this.mapper.funcionalidadeToGETFuncionalidadeResponseDto(this.funcionalidadeRepository.save(funcionalidade));
		
		return response;
	}

	@Override
	public Page<GETFuncionalidadeResponseDto> buscaPaginado(FuncionalidadeFilter filter, Pageable pageable) {

		log.info("Buscando módulo(s)...");
		
		Response<List<GETFuncionalidadeResponseDto>> response = new Response<List<GETFuncionalidadeResponseDto>>(); 
		
		List<GETFuncionalidadeResponseDto> listFuncionalidades = new ArrayList<GETFuncionalidadeResponseDto>();
		
		this.query.query(filter, pageable).forEach(m -> {
			listFuncionalidades.add(this.mapper.funcionalidadeToGETFuncionalidadeResponseDto(m));
		});
		
		response.setData(listFuncionalidades);
		return new PageImpl<>(response.getData(), pageable, this.query.totalRegistros(filter));
		
	}

	@Override
	public GETFuncionalidadeResponseDto busca(FuncionalidadeFilter filter, Pageable pageable) {
		
		
		return null;
	}

	@Override
	public List<GETFuncionalidadeResponseDto> atualizaEmLote(List<AtualizaFuncionalidadeDto> put, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		return null;
	}


}
