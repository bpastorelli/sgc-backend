package br.com.sgc.access.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GETAcessoModuloResponseDto implements Comparable<GETAcessoModuloResponseDto> {
	
	private Long id;
	
	private Long idUsuario;
	
	private Long idModulo;
	
	private String nomeModulo;
	
	private String pathModulo;
	
	private boolean acesso;
	
	private List<GETAcessoFuncionalidadeResponseDto> funcionalidades;
	
	@Override
	public int compareTo(GETAcessoModuloResponseDto o) {
		return this.nomeModulo.compareTo(o.nomeModulo);
	}

}
