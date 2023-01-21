package br.com.sgc.access.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETPerfilFuncionalidadeResponseDto implements Comparable<GETPerfilFuncionalidadeResponseDto> {
	
	private Long idFuncionalidade;
	
	private String nomeFuncionalidade;
	
	private String pathFuncionalidade;
	
	private boolean acesso;
	
	@Override
	public int compareTo(GETPerfilFuncionalidadeResponseDto o) {
		return this.nomeFuncionalidade.compareTo(o.nomeFuncionalidade);
	}

}
