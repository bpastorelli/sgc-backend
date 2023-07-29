package br.com.sgc.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETContribuicaoResponseDto;
import br.com.sgc.entities.Lancamento;
import br.com.sgc.filter.ContribuicaoFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContribuicaoServiceImpl implements ServicesCore<GETContribuicaoResponseDto, ContribuicaoFilter> {

	@Autowired
	private QueryRepository<Lancamento, ContribuicaoFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETContribuicaoResponseDto>, List<Lancamento>> converter;
	
	@Override
	public Page<GETContribuicaoResponseDto> buscar(ContribuicaoFilter filtros, Pageable pageable) {

		log.info("Buscando contribuições...");
		
		Response<List<GETContribuicaoResponseDto>> response = new Response<List<GETContribuicaoResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
		
	}

	@Override
	public Optional<List<GETContribuicaoResponseDto>> buscar(ContribuicaoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
