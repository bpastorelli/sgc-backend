package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.dto.GETVisitanteSemVeiculosResponseDto;
import br.com.sgc.entities.Veiculo;
import br.com.sgc.entities.VinculoVeiculo;
import br.com.sgc.mapper.VisitanteMapper;

@Component
public class ConvertListVeiculoToGETListVeiculoResponseDto implements Converter<List<GETVeiculoResponseDto>, List<Veiculo>> {
	
	@Autowired
	private VisitanteMapper visitanteMapper;
	
	@Override
	public List<GETVeiculoResponseDto> convert(List<Veiculo> veiculos) {

		List<GETVeiculoResponseDto> response = new ArrayList<GETVeiculoResponseDto>();
		
		veiculos.forEach(m -> {
			
			List<GETVisitanteSemVeiculosResponseDto> visitantes = new ArrayList<GETVisitanteSemVeiculosResponseDto>();
			List<VinculoVeiculo> vinculos = m.getVisitantes();
			
			vinculos.forEach(v -> {
				visitantes.add(this.visitanteMapper.visitanteToGETVisitanteSemVeiculosResponseDto(v.getVisitante()));
			});
			
			GETVeiculoResponseDto veiculo = GETVeiculoResponseDto.builder()
					.id(m.getId())
					.placa(m.getPlaca())
					.marca(m.getMarca())
					.modelo(m.getModelo())
					.ano(m.getAno())
					.cor(m.getCor())
					.guide(m.getGuide())
					.posicao(m.getPosicao())
					.visitantes(visitantes)
					.build();
			
			response.add(veiculo);
			
		});
		
		
		return response;
		
	}

}
