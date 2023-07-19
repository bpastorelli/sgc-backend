package br.com.sgc.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.sgc.ContribuicaoAvro;
import br.com.sgc.LancamentoAvro;
import br.com.sgc.converter.Converter;
import br.com.sgc.entities.Lancamento;
import br.com.sgc.utils.Utils;

@Component
public class ConvertListLancamentoToListContribuicaoAvro implements Converter<ContribuicaoAvro, List<Lancamento>> {
	
	@Override
	public ContribuicaoAvro convert(List<Lancamento> lancamentos) {
		

		List<LancamentoAvro> lancamentosAvro = new ArrayList<LancamentoAvro>();
		
		lancamentos.forEach(l -> {
			
			LancamentoAvro lancamento = new LancamentoAvro();
			lancamento.setId(l.getId() == null ? 0 : l.getId());
			lancamento.setDataPagamento(Utils.convertToLong(l.getDataPagamento()));
			lancamento.setPeriodo(l.getPeriodo());
			lancamento.setDocumento(l.getDocumento());
			lancamento.setMoradorId(l.getMorador().getId());
			lancamento.setResidenciaId(l.getResidencia().getId());
			lancamento.setValor(Double.parseDouble(l.getValor().toString()));
			
		});
		
		ContribuicaoAvro contribuicao = ContribuicaoAvro.newBuilder()
				.setLancamentos(lancamentosAvro)
				.build();
		
		
		return contribuicao;
	}

}
