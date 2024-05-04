package br.com.sgc.access.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuloFilter {

	private Long id;
	
	private String descricao;
	
	private String pathModulo;
	
	private Long posicao;
	
	private boolean content;
	
}
