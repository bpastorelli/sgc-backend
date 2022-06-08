package br.com.sgc.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.mapper.ResidenciaMapper;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ResidenciaService;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaServiceImpl implements ResidenciaService<ResidenciaDto> {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFilter> queryRepository;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private Validators<List<ResidenciaDto>> validator;
	
	@Autowired
	private Converter<List<GETResidenciaResponseDto>, List<Residencia>> converter;

	@CachePut(value = "residenciaCache")
	public Response<List<ResidenciaDto>> persistir(List<ResidenciaDto> residenciasDto) throws RegistroException {
		
		log.info("Persistir residencias {}", residenciasDto);
		
		Response<List<ResidenciaDto>> response = new Response<List<ResidenciaDto>>();
		
		this.validator.validar(residenciasDto);
				
		List<Residencia> residencias = this.residenciaMapper.listResidenciaDtoToListResidencia(residenciasDto);
			
		this.residenciaRepository.saveAll(residencias);
		response.setData(this.residenciaMapper.listResidenciaToListResidenciaDto(residencias));
		
		return response;
	}

	@Override
	public Response<ResidenciaDto> buscarPorGuide(String guide) {
		
		log.info("Buscando morador pelo ticket " + guide);
		
		Response<ResidenciaDto> response = new Response<ResidenciaDto>();
		
		ResidenciaDto residenciaDto;
		
		Optional<Residencia> residencia = this.residenciaRepository.findByGuide(guide);
		
		if(residencia.isPresent()) {
			residenciaDto = this.residenciaMapper.residenciaToResidenciaDto(residencia.get());
			response.setData(residenciaDto);
		}
		
		return response;
	}

	@Override
	public Response<ResidenciaDto> persistir(ResidenciaDto residenciaDto) throws RegistroException {

		log.info("Persistir moradores {}", residenciaDto);
		
		Response<ResidenciaDto> response = new Response<ResidenciaDto>();
		
		List<ResidenciaDto> residenciasDto = new ArrayList<ResidenciaDto>();
		
		residenciasDto.add(residenciaDto);
		
		this.validator.validar(residenciasDto);
				
		List<Residencia> residencias = this.residenciaMapper.listResidenciaDtoToListResidencia(residenciasDto);
			
		this.residenciaRepository.saveAll(residencias);
		response.setData(this.residenciaMapper.residenciaToResidenciaDto(residencias.get(0)));
		
		return response;
		
	}

	@Override
	public Page<GETResidenciaResponseDto> buscarResidencia(ResidenciaFilter filtros, Pageable pageable) {
		
		log.info("Buscando residencia(s)...");
		
		Response<List<GETResidenciaResponseDto>> response = new Response<List<GETResidenciaResponseDto>>();
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

}
