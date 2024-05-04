package br.com.sgc.access.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaModuloDto {
	
	@NotEmpty(message = "O campo decrição do módulo é obrigatório")
	private String descricao;
	
	private String pathModulo;
	
	private Long posicao;

}
