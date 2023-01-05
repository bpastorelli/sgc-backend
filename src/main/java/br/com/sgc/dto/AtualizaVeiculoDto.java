package br.com.sgc.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaVeiculoDto implements Serializable {
	

	private static final long serialVersionUID = 1423232434532327L;
	
	private Long id;
	
	private String marca;
	
	private String modelo;
	
	private String cor;
	
	private Long   ano;
	
	private Long   posicao;

}
