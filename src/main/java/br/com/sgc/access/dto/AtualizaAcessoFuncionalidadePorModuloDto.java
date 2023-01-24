package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaAcessoFuncionalidadePorModuloDto {
	
	@NotEmpty(message = "O campo id da funcionalidade é obrigatório")
	private Long idFuncionalidade;
	
	private boolean acesso;

}
