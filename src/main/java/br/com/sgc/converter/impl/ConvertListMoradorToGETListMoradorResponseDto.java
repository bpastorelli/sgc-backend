package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.GETResidenciaSemMoradoresResponseDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.mapper.MoradorMapper;
import br.com.sgc.mapper.ResidenciaMapper;

@Component
public class ConvertListMoradorToGETListMoradorResponseDto implements Converter<List<GETMoradorResponseDto>, List<Morador>> {

	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private MoradorMapper moradorMapper;
	
	@Override
	public List<GETMoradorResponseDto> convert(List<Morador> moradores) {
		
		List<GETMoradorResponseDto> response = new ArrayList<GETMoradorResponseDto>();
		
		moradores.forEach(m -> {
			
			List<GETResidenciaSemMoradoresResponseDto> residencias = new ArrayList<GETResidenciaSemMoradoresResponseDto>();
			List<VinculoResidencia> vinculos = m.getResidencias();
			
			vinculos.forEach(v -> {
				residencias.add(this.residenciaMapper.residenciaToGETResidenciaSemMoradoresResponseDto(v.getResidencia()));
			});
			
			GETMoradorResponseDto morador = new GETMoradorResponseDto();
			morador = this.moradorMapper.moradorToGETMoradorResponseDto(m);
			morador.setResidencias(residencias);
			
			response.add(morador);
			
		});
		
		
		return response;
	}

}
