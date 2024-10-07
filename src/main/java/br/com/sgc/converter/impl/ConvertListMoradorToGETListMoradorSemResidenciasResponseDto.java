package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradorSemResidenciasResponseDto;
import br.com.sgc.dto.GETMoradoresResponseDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.mapper.MoradorMapper;

@Component
public class ConvertListMoradorToGETListMoradorSemResidenciasResponseDto implements Converter<GETMoradoresResponseDto, List<Morador>> {
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	@Override
	public GETMoradoresResponseDto convert(List<Morador> moradores) {
		
		List<GETMoradorSemResidenciasResponseDto> moradoresSemResidencias = new ArrayList<GETMoradorSemResidenciasResponseDto>();
		
		moradores.forEach(m -> {
			GETMoradorSemResidenciasResponseDto morador = new GETMoradorSemResidenciasResponseDto();
			morador = this.moradorMapper.moradorToGETMoradorSemResidenciasResponseDto(m);
			moradoresSemResidencias.add(morador);
		});
		
		GETMoradoresResponseDto moradoresResponse = GETMoradoresResponseDto.builder()
				.moradores(moradoresSemResidencias)
				.build();
		
		return moradoresResponse;
	}

}
