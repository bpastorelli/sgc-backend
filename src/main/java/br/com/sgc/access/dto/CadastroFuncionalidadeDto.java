package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroFuncionalidadeDto {
	
	@NotNull(message = "O campo id módulo da funcionalidade é obrigatório")
	private Long idModulo;
	
	@NotEmpty(message = "O campo decrição da funcionalidade é obrigatório")
	@NotNull(message = "O campo decrição da funcionalidade é obrigatório")
	private String descricao;
	
	@NotEmpty(message = "O campo Path da funcionalidade é obrigatório")
	@NotNull(message = "O campo Path da funcionalidade é obrigatório")
	private String pathFuncionalidade;

}
