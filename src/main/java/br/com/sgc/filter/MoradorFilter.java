package br.com.sgc.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoradorFilter {
	
	private Long id;
	
	private String nome;
	
	private String cpf;
	
	private String rg;
	
	private Long posicao;
	
	private boolean content;

}
