package br.com.sgc.access.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcessoFuncionalidadeUsuarioResponseDto implements Comparable<AcessoFuncionalidadeUsuarioResponseDto> {
	
	private Long idModulo;
	
	private Long idFuncionalidade;
	
	private String funcionalidade;
	
	private String path;
	
	private boolean acesso;

	@Override
	public int compareTo(AcessoFuncionalidadeUsuarioResponseDto o) {
		return this.funcionalidade.compareTo(o.funcionalidade);
	}

}
