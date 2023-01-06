package br.com.sgc.converter.impl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETVisitaResponseDto;
import br.com.sgc.entities.Visita;
import br.com.sgc.mapper.VeiculoMapper;
import br.com.sgc.utils.Utils;

@Component
public class ConvertListVisitaToGETListVisitaResponseDto implements Converter<List<GETVisitaResponseDto>, List<Visita>> {
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	@Override
	public List<GETVisitaResponseDto> convert(List<Visita> visitas) {

		List<GETVisitaResponseDto> response = new ArrayList<GETVisitaResponseDto>();
		
		visitas.forEach(m -> {
			
			GETVisitaResponseDto visitante = GETVisitaResponseDto.builder()
					.id(m.getId())
					.nome(m.getVisitante().getNome().toUpperCase())
					.rg(m.getVisitante().getRg())
					.cpf(m.getVisitante().getCpf() != null ? m.getVisitante().getCpf() : "")
					.dataEntrada(Utils.dateFormat(m.getDataEntrada(), "dd/MM/yyyy"))
					.horaEntrada(new Time(m.getDataEntrada().getTime()))
					.dataSaida(m.getDataSaida() != null ? Utils.dateFormat(m.getDataSaida(), "dd/MM/yyyy") : "")
					.horaSaida(m.getHoraSaida() != null ? new Time(m.getDataSaida().getTime()) : null)
					.endereco(m.getResidencia().getEndereco() != null ? m.getResidencia().getEndereco() : "")
					.numero(m.getResidencia().getNumero().toString() != null ? m.getResidencia().getNumero().toString() : "")
					.complemento(m.getResidencia().getComplemento().toString() != null ? m.getResidencia().getComplemento().toString() : "")
					.bairro(m.getResidencia().getBairro())
					.cidade(m.getResidencia().getCidade())
					.uf(m.getResidencia().getUf())
					.placa(Utils.formatPlaca(m.getPlaca()))
					.posicao(m.getPosicao())
					.veiculo(!m.getPlaca().isBlank() ? 
							veiculoMapper.veiculoToGETVeiculoResponseDto(
									m.getVisitante().getVeiculos()
										.stream()
										.filter(p -> p.getVeiculo().getPlaca().trim().equals(m.getPlaca().trim()))
										.findFirst()
										.get()
										.getVeiculo()) : null)
					.guide(m.getGuide())
					.build();
			
			response.add(visitante);
			
		});
		
		
		return response;
		
	}

}
