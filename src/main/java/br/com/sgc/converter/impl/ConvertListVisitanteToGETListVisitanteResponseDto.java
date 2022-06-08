package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.dto.GETVisitanteResponseDto;
import br.com.sgc.entities.VinculoVeiculo;
import br.com.sgc.entities.Visitante;
import br.com.sgc.mapper.VeiculoMapper;

@Component
public class ConvertListVisitanteToGETListVisitanteResponseDto implements Converter<List<GETVisitanteResponseDto>, List<Visitante>> {
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	@Override
	public List<GETVisitanteResponseDto> convert(List<Visitante> visitantes) {
		
		List<GETVisitanteResponseDto> response = new ArrayList<GETVisitanteResponseDto>();
		
		visitantes.forEach(m -> {
			
			List<GETVeiculoResponseDto> veiculos = new ArrayList<GETVeiculoResponseDto>();
			List<VinculoVeiculo> vinculos = m.getVeiculos();
			
			vinculos.forEach(v -> {
				veiculos.add(this.veiculoMapper.veiculoToGETVeiculoResponseDto(v.getVeiculo()));
			});
			
			GETVisitanteResponseDto visitante = GETVisitanteResponseDto.builder()
					.id(m.getId())
					.nome(m.getNome())
					.cpf(m.getCpf())
					.rg(m.getRg())
					.endereco(m.getEndereco())
					.numero(m.getNumero())
					.complemento(m.getComplemento())
					.bairro(m.getBairro())
					.cep(m.getCep())
					.cidade(m.getCidade())
					.uf(m.getUf())
					.telefone(m.getTelefone())
					.celular(m.getCelular())
					.guide(m.getGuide())
					.posicao(m.getPosicao())
					.veiculos(veiculos)
					.build();
			
			response.add(visitante);
			
		});
		
		
		return response;
	}

}
