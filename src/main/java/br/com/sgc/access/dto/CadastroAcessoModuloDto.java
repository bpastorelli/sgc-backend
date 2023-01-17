package br.com.sgc.access.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroAcessoModuloDto {
	
	@NotEmpty(message = "O campo id do usuário é obrigatório")
	private Long idUsuario;
	
	@NotEmpty(message = "O campo id do módulo é obrigatório")
	private Long idModulo;
	
	private boolean acesso;
	
	@Size(min = 1, message = "Você deve incluir ao menos uma funcionalidade.")
	private List<CadastroAcessoFuncionalidadeDto> funcionalidades;

}
