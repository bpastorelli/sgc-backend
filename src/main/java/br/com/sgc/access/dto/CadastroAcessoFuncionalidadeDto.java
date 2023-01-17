package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroAcessoFuncionalidadeDto {
	
	@NotEmpty(message = "O campo id do usuário é obrigatório")
	private Long idUsuario;
	
	@NotEmpty(message = "O campo id do módulo é obrigatório")
	private Long idModulo;
	
	@NotEmpty(message = "O campo id da funcionalidade é obrigatório")
	private Long idFuncionalidade;
	
	private boolean acesso;

}
