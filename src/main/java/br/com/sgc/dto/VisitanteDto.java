package br.com.sgc.dto;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitanteDto {
	
	@NotNull(message = "Campo nome é obrigatório")
	private String nome;
	
	@NotNull(message = "Campo RG é obrigatório")
	private String rg;
	
	private String cpf;
	
	@NotNull(message = "Campo CEP é obrigatório")
	private String cep;
	
	@NotNull(message = "Campo endereço é obrigatório")
	private String endereco;
	
	@NotNull(message = "Campo número é obrigatório")
	private String numero;
	
	private String complemento;
	
	@NotNull(message = "Campo bairro é obrigatório")
	private String bairro;
	
	@NotNull(message = "Campo cidade é obrigatório")
	private String cidade;
	
	@NotNull(message = "Campo uf é obrigatório")
	private String uf;
	
	private String telefone;
	
	private String celular;
	
	@Transient
	private String guide;

}
