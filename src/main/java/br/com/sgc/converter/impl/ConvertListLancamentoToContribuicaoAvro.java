package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.sgc.ContribuicaoAvro;
import br.com.sgc.LancamentoAvro;
import br.com.sgc.converter.Converter;
import br.com.sgc.dto.LancamentoDto;

@Component
public class ConvertListLancamentoToContribuicaoAvro implements Converter<ContribuicaoAvro, List<LancamentoDto>> {
	
	@Override
	public ContribuicaoAvro convert(List<LancamentoDto> lancamentos) {

		List<LancamentoAvro> lancamentosAvro = new ArrayList<LancamentoAvro>();
		
		lancamentos.forEach(l -> {
			
			LancamentoAvro lancamento = new LancamentoAvro();
			lancamento.setId(l.getMorador().getId() == null ? 0 : l.getMorador().getId());
			lancamento.setDataPagamento(l.getDataPagamento().toString());
			lancamento.setPeriodo(l.getPeriodo());
			lancamento.setDocumento(l.getDocumento());
			lancamento.setMoradorId(l.getMorador().getId());
			lancamento.setResidenciaId(l.getResidencia().getId());
			lancamento.setValor(Double.parseDouble(l.getValor().toString()));
			lancamentosAvro.add(lancamento);
			
		});
		
		ContribuicaoAvro contribuicao = ContribuicaoAvro.newBuilder()
				.setRequisicaoId(lancamentos.get(0).getRequisicaoId())
				.setPage(lancamentos.get(0).getPage())
				.setTotalPages(lancamentos.get(0).getTotalPages())
				.setLancamentos(lancamentosAvro)
				.build();	
		
		return contribuicao;
	}

}
