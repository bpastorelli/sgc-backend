package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradorSemResidenciasResponseDto;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.mapper.ResidenciaMapper;

@Component
public class ConvertListResidenciaToGETListResidenciaResponseDto implements Converter<List<GETResidenciaResponseDto>, List<Residencia>> {

	@Autowired
	private MoradorMapper moradorMapper;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Override
	public List<GETResidenciaResponseDto> convert(List<Residencia> residencias) {
		
		List<GETResidenciaResponseDto> response = new ArrayList<GETResidenciaResponseDto>();
		
		residencias.forEach(m -> {
			
			List<GETMoradorSemResidenciasResponseDto> moradores = new ArrayList<GETMoradorSemResidenciasResponseDto>();
			List<VinculoResidencia> vinculos = m.getMoradores();
			
			vinculos.forEach(v -> {
				moradores.add(this.moradorMapper.moradorToGETMoradorSemResidenciasResponseDto(v.getMorador()));
			});
			
			GETResidenciaResponseDto residencia = new GETResidenciaResponseDto();
			residencia = this.residenciaMapper.residenciaToGETResidenciaResponseDto(m);
			residencia.setMoradores(moradores);
			
			response.add(residencia);
			
		});
		
		
		return response;
	}

}
