package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.mapper.MoradorMapper;

@Component
public class ConvertListResidenciaToGETListResidenciaResponseDto implements Converter<List<GETResidenciaResponseDto>, List<Residencia>> {

	@Autowired
	private MoradorMapper moradorMapper;
	
	@Override
	public List<GETResidenciaResponseDto> convert(List<Residencia> residencias) {
		
		List<GETResidenciaResponseDto> response = new ArrayList<GETResidenciaResponseDto>();
		
		residencias.forEach(m -> {
			
			List<MoradorDto> moradores = new ArrayList<MoradorDto>();
			List<VinculoResidencia> vinculos = m.getMoradores();
			
			vinculos.forEach(v -> {
				moradores.add(this.moradorMapper.moradorToMoradorDto(v.getMorador()));
			});
			
			GETResidenciaResponseDto residencia = GETResidenciaResponseDto.builder()
					.id(m.getId())
					.endereco(m.getEndereco())
					.numero(m.getNumero())
					.complemento(m.getComplemento())
					.bairro(m.getBairro())
					.cep(m.getCep())
					.cidade(m.getCidade())
					.uf(m.getUf())
					.moradores(moradores)
					.guide(m.getGuide())
					.build();
			
			response.add(residencia);
			
		});
		
		
		return response;
	}

}
