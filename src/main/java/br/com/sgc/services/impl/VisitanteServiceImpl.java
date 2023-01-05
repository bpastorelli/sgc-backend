package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVisitanteResponseDto;
import br.com.sgc.entities.Visitante;
import br.com.sgc.filter.VisitanteFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.Services;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitanteServiceImpl implements Services<GETVisitanteResponseDto, VisitanteFilter> {
	
	@Autowired
	private QueryRepository<Visitante, VisitanteFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETVisitanteResponseDto>, List<Visitante>> converter;

	@Override
	public Page<GETVisitanteResponseDto> buscar(VisitanteFilter filtros, Pageable pageable) {
		
		log.info("Buscando visitantes(es)...");
		
		Response<List<GETVisitanteResponseDto>> response = new Response<List<GETVisitanteResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
	}

}
