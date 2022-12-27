package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVisitaResponseDto;
import br.com.sgc.entities.Visita;
import br.com.sgc.filter.VisitaFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.VisitaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitaServiceImpl implements VisitaService<GETVisitaResponseDto, VisitaFilter> {

	@Autowired
	private QueryRepository<Visita, VisitaFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETVisitaResponseDto>, List<Visita>> converter;
	
	@Override
	public Page<GETVisitaResponseDto> buscarVisitas(VisitaFilter filtros, Pageable pageable) {

		log.info("Buscando visita(s)...");
		
		Response<List<GETVisitaResponseDto>> response = new Response<List<GETVisitaResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
		
	}

}
