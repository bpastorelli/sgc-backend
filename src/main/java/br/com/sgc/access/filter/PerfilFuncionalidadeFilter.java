package br.com.sgc.access.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilFuncionalidadeFilter {

	private Long id;
	
	private Long idUsuario;
	
	private Long idFuncionalidade;
	
	private Long idModulo;
	
	private String path;
	
	private Long posicao;
	
	private boolean content;
	
}
