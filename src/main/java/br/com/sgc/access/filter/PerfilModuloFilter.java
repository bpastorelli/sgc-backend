package br.com.sgc.access.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilModuloFilter {

	private Long id;
	
	private Long idUsuario;
	
	private Long idModulo;
	
	private String path;
	
	private Long posicao;
	
	private boolean content;
	
}
