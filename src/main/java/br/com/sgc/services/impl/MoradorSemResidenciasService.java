package br.com.sgc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradoresResponseDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.repositories.MoradorRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorSemResidenciasService {
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private Converter<GETMoradoresResponseDto, List<Morador>> converter;

	public GETMoradoresResponseDto buscar(Long residenciaId) {
		
		log.info("Buscando morador(es) por residencia id {}", residenciaId); 
		
		return this.converter.convert(this.moradorRepository.findByResidenciaId(residenciaId));
	}

}
