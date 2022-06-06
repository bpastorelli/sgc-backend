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
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.queries.QueryRepository;
import br.com.sgc.response.Response;
import br.com.sgc.services.MoradorService;
import br.com.sgc.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorServiceImpl implements MoradorService<MoradorDto> {
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private QueryRepository<Morador, MoradorFilter> queryRepository;
	
	@Autowired
	private Converter<List<GETMoradorResponseDto>, List<Morador>> converter;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	@Autowired
	private Validators<List<MoradorDto>> validator;

	@CachePut(value = "moradorCache")
	public Response<List<GETMoradorResponseDto>> persistir(List<MoradorDto> moradoresDto) throws RegistroException {
		
		log.info("Persistir moradores {}", moradoresDto);
		
		Response<List<GETMoradorResponseDto>> response = new Response<List<GETMoradorResponseDto>>();
		
		this.validator.validar(moradoresDto);
				
		List<Morador> moradores = this.moradorMapper.listMoradorDtoToListMorador(moradoresDto);
		moradores.forEach(m -> {
			m.setSenha(m.getCpf().substring(6));
		});
			
		this.moradorRepository.saveAll(moradores);
		response.setData(this.moradorMapper.listMoradorToListGETMoradorResponseDto(moradores));
		
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
		
		this.validator.validar(moradoresDto);
					
		List<Morador> moradores = this.moradorMapper.listMoradorDtoToListMorador(moradoresDto);
		moradores.forEach(m -> {
			m.setSenha(m.getCpf().substring(6));
		});
			
		this.moradorRepository.saveAll(moradores);
		response.setData(this.moradorMapper.moradorToMoradorDto(moradores.get(0)));
		
		return response;
		
	}

	@Override
	public Page<GETMoradorResponseDto> buscarMorador(MoradorFilter filtros, Pageable pageable) {
		
		log.info("Buscando morador(es)...");
		
		Response<List<GETMoradorResponseDto>> response = new Response<List<GETMoradorResponseDto>>(); 
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		return new PageImpl<>(response.getData(), pageable, this.queryRepository.totalRegistros(filtros));
	}

}
