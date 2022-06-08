package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Morador;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.mapper.ResidenciaMapper;

@Component
public class ConvertListMoradorToGETListMoradorResponseDto implements Converter<List<GETMoradorResponseDto>, List<Morador>> {

	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Override
	public List<GETMoradorResponseDto> convert(List<Morador> moradores) {
		
		List<GETMoradorResponseDto> response = new ArrayList<GETMoradorResponseDto>();
		
		moradores.forEach(m -> {
			
			List<ResidenciaDto> residencias = new ArrayList<ResidenciaDto>();
			List<VinculoResidencia> vinculos = m.getResidencias();
			
			vinculos.forEach(v -> {
				residencias.add(this.residenciaMapper.residenciaToResidenciaDto(v.getResidencia()));
			});
			
			GETMoradorResponseDto morador = GETMoradorResponseDto.builder()
					.id(m.getId())
					.nome(m.getNome())
					.cpf(m.getCpf())
					.rg(m.getRg())
					.email(m.getEmail())
					.associado(m.getAssociado())
					.telefone(m.getTelefone())
					.celular(m.getCelular())
					.guide(m.getGuide())
					.perfil(m.getPerfil())
					.posicao(m.getPosicao())
					.residencias(residencias)
					.build();
			
			response.add(morador);
			
		});
		
		
		return response;
	}

}
