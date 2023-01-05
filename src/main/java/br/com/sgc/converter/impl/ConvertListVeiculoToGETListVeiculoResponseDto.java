package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.entities.Veiculo;
import br.com.sgc.mapper.VeiculoMapper;

@Component
public class ConvertListVeiculoToGETListVeiculoResponseDto implements Converter<List<GETVeiculoResponseDto>, List<Veiculo>> {
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	@Override
	public List<GETVeiculoResponseDto> convert(List<Veiculo> veiculos) {

		List<GETVeiculoResponseDto> response = new ArrayList<GETVeiculoResponseDto>();
		
		veiculos.forEach(m -> {
			
			response.add(this.veiculoMapper.veiculoToGETVeiculoResponseDto(m));
			
		});
		
		
		return response;
		
	}

}
