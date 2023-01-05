package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.entities.Veiculo;
import br.com.sgc.filter.VeiculoFilter;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.Services;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeiculoServiceImpl implements Services<GETVeiculoResponseDto, VeiculoFilter> {
	
	@Autowired
	private QueryRepository<Veiculo, VeiculoFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETVeiculoResponseDto>, List<Veiculo>> converter;
	
	@Override
	public Page<GETVeiculoResponseDto> buscar(VeiculoFilter filtros, Pageable pageable) {

		log.info("Buscando visita(s)...");
		
		Response<List<GETVeiculoResponseDto>> response = new Response<List<GETVeiculoResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
		
	}

}
