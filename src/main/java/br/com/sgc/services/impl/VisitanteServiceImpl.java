package br.com.sgc.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVisitanteResponseDto;
import br.com.sgc.entities.Visitante;
import br.com.sgc.filter.VisitanteFilter;
import br.com.sgc.mapper.VisitanteMapper;
import br.com.sgc.repositories.VisitanteRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.VisitanteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitanteServiceImpl implements VisitanteService<GETVisitanteResponseDto> {
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private QueryRepository<Visitante, VisitanteFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETVisitanteResponseDto>, List<Visitante>> converter;
	
	@Autowired
	private VisitanteMapper visitanteMapper;

	@Override
	public Response<GETVisitanteResponseDto> buscarPorGuide(String guide) {
		
		log.info("Buscando morador pelo ticket " + guide);
		
		Response<GETVisitanteResponseDto> response = new Response<GETVisitanteResponseDto>();
		
		GETVisitanteResponseDto visitanteDto;
		
		Optional<Visitante> visitante = this.visitanteRepository.findByGuide(guide);
		
		if(visitante.isPresent()) {
			visitanteDto = this.visitanteMapper.visitanteToGETVisitanteResponseDto(visitante.get());
			response.setData(visitanteDto);
		}
		
		return response;
	}

	@Override
	public Page<GETVisitanteResponseDto> buscarVisitante(VisitanteFilter filtros, Pageable pageable) {
		
		log.info("Buscando visitantes(es)...");
		
		Response<List<GETVisitanteResponseDto>> response = new Response<List<GETVisitanteResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
	}

}
