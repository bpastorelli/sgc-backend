package br.com.sgc.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaServiceImpl implements ServicesCore<GETResidenciaResponseDto, ResidenciaFilter> {
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETResidenciaResponseDto>, List<Residencia>> converter;

	@Override
	public Page<GETResidenciaResponseDto> buscar(ResidenciaFilter filtros, Pageable pageable) {
		
		log.info("Buscando residencia(s)...");
		
		Response<List<GETResidenciaResponseDto>> response = new Response<List<GETResidenciaResponseDto>>();
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

	@Override
	public Optional<List<GETResidenciaResponseDto>> buscar(ResidenciaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
