package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroModuloDto {
	
	@NotEmpty(message = "O campo decrição do módulo é obrigatório")
	private String descricao;
	
	@NotEmpty(message = "O campo path do módulo é obrigatório")
	private String pathModulo;

}
