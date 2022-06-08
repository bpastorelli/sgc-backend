package br.com.sgc.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoCadastroDto {
	
	@JsonUnwrapped
	private MoradorDto morador;
	
	private ResidenciaDto residencia;
	
	private String guide;

}
