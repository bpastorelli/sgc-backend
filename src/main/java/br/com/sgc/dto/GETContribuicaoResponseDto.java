package br.com.sgc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GETContribuicaoResponseDto {
	
	private Long id;
	
	private String nome;
	
	private String rg;
	
	private String cpf;
	
	private String dataPagamento;
	
	private String periodo;
	
	private String valor;
	
	private String documento;
	
	private String endereco;
	
	private Long numero;
	
	private String bairro;
	
	private String complemento;
	
	private String dataCadastro;
	

}
