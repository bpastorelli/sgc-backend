package br.com.sgc.access.dto;

import br.com.sgc.enums.FuncaoFuncionalidadeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GETFuncionalidadeResponseDto {

	private Long id;
	
	private Long idModulo;
	
	private String descricao;
	
	private String pathFuncionalidade;
	
	private FuncaoFuncionalidadeEnum funcao; 
	
	private Long posicao;
	
}
