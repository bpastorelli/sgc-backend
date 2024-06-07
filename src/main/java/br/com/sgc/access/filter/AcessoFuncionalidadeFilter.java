package br.com.sgc.access.filter;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AcessoFuncionalidadeFilter {

	private Long id;
	
	private Long idUsuario;
	
	private List<Long> idModulo;
	
	private List<Long> idFuncionalidade;
	
	private String path;
	
	@Builder.Default
	private boolean acesso = true;
	
	private Long posicao;
	
	private boolean content;
	
}
