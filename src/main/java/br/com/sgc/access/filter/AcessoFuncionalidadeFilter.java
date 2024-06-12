package br.com.sgc.access.filter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcessoFuncionalidadeFilter {

	private Long id;
	
	private Long idUsuario;
	
	private List<Long> idModulo;
	
	private List<Long> idFuncionalidade;
	
	private String path;
	
	private boolean acesso;
	
	private Long posicao;
	
	private boolean content;
	
}
