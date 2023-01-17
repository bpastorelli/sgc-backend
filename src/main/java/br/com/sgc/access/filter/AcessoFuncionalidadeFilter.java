package br.com.sgc.access.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AcessoFuncionalidadeFilter {

	private Long id;
	
	private Long idUsuario;
	
	private Long idModulo;
	
	private Long idFuncionalidade;
	
	private String path;
	
	private boolean acesso;
	
	private Long posicao;
	
	private boolean content;
	
}
