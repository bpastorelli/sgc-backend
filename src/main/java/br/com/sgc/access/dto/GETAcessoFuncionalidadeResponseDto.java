package br.com.sgc.access.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GETAcessoFuncionalidadeResponseDto implements Comparable<GETAcessoFuncionalidadeResponseDto> {
	
	private Long id;
	
	private Long idModulo;
	
	private String nomeModulo;
	
	private Long idFuncionalidade;
	
	private String nomeFuncionalidade;
	
	private String pathFuncionalidade;
	
	private boolean acesso;
	
	private boolean inclusao;
	
	private boolean alteracao;
	
	private boolean exclusao;
	
	@Override
	public int compareTo(GETAcessoFuncionalidadeResponseDto o) {
		return this.nomeFuncionalidade.compareTo(o.nomeFuncionalidade);
	}

}
