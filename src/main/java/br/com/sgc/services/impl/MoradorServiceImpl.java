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

import br.com.sgc.dto.MoradorDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.MoradorService;
import br.com.sgc.validators.Validators;

@Service
public class MoradorServiceImpl implements MoradorService<MoradorDto> {

	private static final Logger log = LoggerFactory.getLogger(MoradorServiceImpl.class);
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private QueryRepository<Morador, MoradorFilter> queryRepository;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	@Autowired
	private Validators<List<MoradorDto>> validator;

	@CachePut(value = "moradorCache")
	public Response<List<MoradorDto>> persistir(List<MoradorDto> moradoresDto) throws RegistroException {
		
		log.info("Persistir moradores {}", moradoresDto);
		
		Response<List<MoradorDto>> response = new Response<List<MoradorDto>>();
		
		List<ErroRegistro> errors = this.validator.validar(moradoresDto);
		
		if(errors.size() == 0) {			
			List<Morador> moradores = this.moradorMapper.listMoradorDtoToListMorador(moradoresDto);
			moradores.forEach(m -> {
				m.setSenha(m.getCpf().substring(6));
			});
			
			this.moradorRepository.saveAll(moradores);
			response.setData(this.moradorMapper.listMoradorToListMoradorDto(moradores));
		}else {			
			response.setErrors(errors);
		}
		
		return response;
	}

	@Override
	public Response<MoradorDto> buscarPorGuide(String guide) {
		
		log.info("Buscando morador pelo ticket " + guide);
		
		Response<MoradorDto> response = new Response<MoradorDto>();
		
		MoradorDto moradorDto;
		
		Optional<Morador> morador = this.moradorRepository.findByGuide(guide);
		
		if(morador.isPresent()) {
			moradorDto = this.moradorMapper.moradorToMoradorDto(morador.get());
			response.setData(moradorDto);
		}
		
		return response;
	}

	@Override
	public Response<MoradorDto> persistir(MoradorDto morador) throws RegistroException {

		log.info("Persistir moradores {}", morador);
		
		Response<MoradorDto> response = new Response<MoradorDto>();
		
		List<MoradorDto> moradoresDto = new ArrayList<MoradorDto>();
		
		moradoresDto.add(morador);
		
		List<ErroRegistro> errors = this.validator.validar(moradoresDto);
		
		if(errors.size() == 0) {			
			List<Morador> moradores = this.moradorMapper.listMoradorDtoToListMorador(moradoresDto);
			moradores.forEach(m -> {
				m.setSenha(m.getCpf().substring(6));
			});
			
			this.moradorRepository.saveAll(moradores);
			response.setData(this.moradorMapper.moradorToMoradorDto(moradores.get(0)));
		}else {			
			response.setErrors(errors);
		}
		
		return response;
		
	}

	@Override
	public Page<MoradorDto> buscarMorador(MoradorFilter filtros, Pageable pageable) {
		
		log.info("Buscando morador(es)...");
		
		Response<List<MoradorDto>> response = new Response<List<MoradorDto>>();
		
		response.setData(this.moradorMapper.listMoradorToListMoradorDto(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

}
