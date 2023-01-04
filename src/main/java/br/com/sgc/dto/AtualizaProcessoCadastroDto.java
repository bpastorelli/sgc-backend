package br.com.sgc.dto;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaProcessoCadastroDto {
	
	private Long id;
	
	@JsonUnwrapped
	private MoradorDto morador;
	
	private ResidenciaDto residencia;
	
	@Transient
	private String guide;

}
