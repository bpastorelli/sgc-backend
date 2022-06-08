package br.com.sgc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETVeiculoResponseDto implements Serializable {
	

	private static final long serialVersionUID = 1423232434532327L;
	
	@NotNull(message = "Campo Placa é obrigatório!")
	private String placa;
	
	private String marca;
	
	@NotNull(message = "O campo Modelo é obrigatório!")
	private String modelo;
	
	@NotNull(message = "O campo Cor é obrigatório!")
	private String cor;
	
	private Long   ano;
	
	private String guide;

}
