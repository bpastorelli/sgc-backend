package br.com.sgc.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResidenciaFilter {
	
	private Long id;
	
	private String endereco;
	
	private Long numero;
	
	private String cep;
	
	private String cidade;
	
	private String uf;
	
	private boolean content;

}
