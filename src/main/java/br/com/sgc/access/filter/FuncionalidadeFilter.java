package br.com.sgc.access.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionalidadeFilter {

	private Long id;
	
	private String descricao;
	
	private String pathFuncionalidade;
	
	private Long posicao;
	
	private boolean content;
	
}
