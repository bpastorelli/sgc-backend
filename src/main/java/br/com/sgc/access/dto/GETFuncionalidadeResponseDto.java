package br.com.sgc.access.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GETFuncionalidadeResponseDto {

	private Long id;
	
	private Long idModulo;
	
	private String descricao;
	
	private String pathFuncionalidade;
	
	private Long posicao;
	
}
