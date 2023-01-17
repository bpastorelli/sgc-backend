package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaAcessoModuloDto {
	
	@NotEmpty(message = "O campo id do módulo é obrigatório")
	private Long idModulo;
	
	private boolean acesso;

}
