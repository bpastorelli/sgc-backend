package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.mapper.ResidenciaMapper;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.Services;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaServiceImpl implements Services<GETResidenciaResponseDto, ResidenciaFilter> {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFilter> queryRepository;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private Validators<List<ResidenciaDto>, List<AtualizaResidenciaDto>> validator;
	
	@Autowired
	private Converter<List<GETResidenciaResponseDto>, List<Residencia>> converter;

	@CachePut(value = "residenciaCache")
	public Response<List<ResidenciaDto>> persistir(List<ResidenciaDto> residenciasDto) throws RegistroException {
		
		log.info("Persistir residencias {}", residenciasDto);
		
		Response<List<ResidenciaDto>> response = new Response<List<ResidenciaDto>>();
		
		this.validator.validarPost(residenciasDto);
				
		List<Residencia> residencias = this.residenciaMapper.listResidenciaDtoToListResidencia(residenciasDto);
			
		this.residenciaRepository.saveAll(residencias);
		response.setData(this.residenciaMapper.listResidenciaToListResidenciaDto(residencias));
		
		return response;
	}

	@Override
	public Page<GETResidenciaResponseDto> buscar(ResidenciaFilter filtros, Pageable pageable) {
		
		log.info("Buscando residencia(s)...");
		
		Response<List<GETResidenciaResponseDto>> response = new Response<List<GETResidenciaResponseDto>>();
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

}
