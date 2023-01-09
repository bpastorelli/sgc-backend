package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorServiceImpl implements ServicesCore<GETMoradorResponseDto, MoradorFilter> {
	
	@Autowired
	private QueryRepository<Morador, MoradorFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETMoradorResponseDto>, List<Morador>> converter;

	@Override
	public Page<GETMoradorResponseDto> buscar(MoradorFilter filtros, Pageable pageable) {
		
		log.info("Buscando morador(es)...");
		
		Response<List<GETMoradorResponseDto>> response = new Response<List<GETMoradorResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
	}

}
