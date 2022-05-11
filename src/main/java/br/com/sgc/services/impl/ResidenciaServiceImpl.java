package br.com.sgc.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.mapper.ResidenciaMapper;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.ResidenciaService;
import br.com.sgc.validators.Validators;

@Service
public class ResidenciaServiceImpl implements ResidenciaService<ResidenciaDto> {

	private static final Logger log = LoggerFactory.getLogger(ResidenciaServiceImpl.class);
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFilter> queryRepository;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private Validators<List<ResidenciaDto>> validator;

	@CachePut(value = "residenciaCache")
	public Response<List<ResidenciaDto>> persistir(List<ResidenciaDto> residenciasDto) throws RegistroException {
		
		log.info("Persistir residencias {}", residenciasDto);
		
		Response<List<ResidenciaDto>> response = new Response<List<ResidenciaDto>>();
		
		List<ErroRegistro> errors = this.validator.validar(residenciasDto);
		
		if(errors.size() == 0) {			
			List<Residencia> residencias = this.residenciaMapper.listResidenciaDtoToListResidencia(residenciasDto);
			
			this.residenciaRepository.saveAll(residencias);
			response.setData(this.residenciaMapper.listResidenciaToListResidenciaDto(residencias));
		}else {			
			response.setErrors(errors);
		}
		
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
		
		List<ErroRegistro> errors = this.validator.validar(residenciasDto);
		
		if(errors.size() == 0) {			
			List<Residencia> residencias = this.residenciaMapper.listResidenciaDtoToListResidencia(residenciasDto);
			
			this.residenciaRepository.saveAll(residencias);
			response.setData(this.residenciaMapper.residenciaToResidenciaDto(residencias.get(0)));
		}else {			
			response.setErrors(errors);
		}
		
		return response;
		
	}

	@Override
	public Page<ResidenciaDto> buscarResidencia(ResidenciaFilter filtros, Pageable pageable) {
		
		log.info("Buscando residencia(s)...");
		
		Response<List<ResidenciaDto>> response = new Response<List<ResidenciaDto>>();
		
		response.setData(this.residenciaMapper.listResidenciaToListResidenciaDto(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

}
