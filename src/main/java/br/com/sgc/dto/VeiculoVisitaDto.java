package br.com.sgc.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoVisitaDto {
	
	@NotNull(message = "O campo Marca é obrigatório!")
	private String marca;
	
	@NotNull(message = "O campo Modelo é obrigatório!")
	private String modelo;
	
	@NotNull(message = "O campo Cor é obrigatório!")
	private String cor;
	
	private Long   ano;

}
