package br.com.sgc.access.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDto {
	
	private Long id;
	
	private String nome;
	
	private String login;
	
	private String token;
	
	private boolean primeiroAcesso;
	

}
