package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.sgc.converter.Converter;
import br.com.sgc.dto.GETContribuicaoResponseDto;
import br.com.sgc.entities.Lancamento;
import br.com.sgc.utils.Utils;

@Component
public class ConvertListLancamentoToGETListContribuicaoResponseDto implements Converter<List<GETContribuicaoResponseDto>, List<Lancamento>> {
	
	@Override
	public List<GETContribuicaoResponseDto> convert(List<Lancamento> lancamentos) {

		List<GETContribuicaoResponseDto> response = new ArrayList<GETContribuicaoResponseDto>();
		
		lancamentos.forEach(m -> {
			
			GETContribuicaoResponseDto lancamento = GETContribuicaoResponseDto.builder()
					.id(m.getId())
					.nome(m.getMorador().getNome().toUpperCase())
					.rg(m.getMorador().getRg())
					.cpf(m.getMorador().getCpf())
					.dataPagamento(Utils.dateFormat(m.getDataPagamento(), "dd/MM/yyyy"))
					.endereco(m.getResidencia().getEndereco().toUpperCase())
					.numero(m.getResidencia().getNumero())
					.bairro(m.getResidencia().getBairro().toUpperCase())
					.complemento(m.getResidencia().getComplemento().toUpperCase())
					.periodo(m.getPeriodo())
					.valor(Utils.formatNumber(m.getValor()))
					.documento(m.getDocumento())
					.dataCadastro(Utils.dateFormat(m.getDataCriacao(), "dd/MM/yyyy"))
					.build();
			
			response.add(lancamento);
			
		});
		
		
		return response;
		
	}

}
