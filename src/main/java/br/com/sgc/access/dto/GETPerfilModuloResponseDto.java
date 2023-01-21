package br.com.sgc.access.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETPerfilModuloResponseDto implements Comparable<GETPerfilModuloResponseDto> {
	
	private Long idModulo;
	
	private String nomeModulo;
	
	private String pathModulo;
	
	private boolean acesso;
	
	@Override
	public int compareTo(GETPerfilModuloResponseDto o) {
		return this.nomeModulo.compareTo(o.nomeModulo);
	}

}
