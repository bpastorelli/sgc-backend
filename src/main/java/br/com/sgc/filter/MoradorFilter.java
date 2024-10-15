package br.com.sgc.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MoradorFilter {
	
	private Long id;
	
	private String nome;
	
	private String cpf;
	
	private String rg;
	
	private String email;
	
	private Long posicao;
	
	private String guide;
	
	private Long residenciaId;
	
	private boolean content;

}
