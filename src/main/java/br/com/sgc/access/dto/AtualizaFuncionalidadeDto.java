package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import br.com.sgc.enums.FuncaoFuncionalidadeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaFuncionalidadeDto {
	
	@NotEmpty(message = "O campo decrição da funcionalidade é obrigatório")
	private String descricao;
	
	@NotEmpty(message = "O campo path da funcionalidade é obrigatório")
	private String pathFuncionalidade;
	
	private FuncaoFuncionalidadeEnum funcao;
	
	private Long posicao;

}
