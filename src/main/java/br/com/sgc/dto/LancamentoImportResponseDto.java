package br.com.sgc.dto;

import lombok.Data;

@Data
public class LancamentoImportResponseDto implements Comparable<LancamentoImportResponseDto> {
	
	private Long id;
	
	private String nome;
	
	private String cpf;
	
	private String dataPagamento;
	
	private String documento;
	
	private String valor;
	
	private String endereco;

	@Override
	public int compareTo(LancamentoImportResponseDto o) {
		return this.getNome().compareTo(o.getNome());
	}
	
}
