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
public class VisitaDto {
	
	@Transient
	private Long id;
	
	private String guide;
	
	@NotNull(message = "Campo RG é obrigatório!")
	private String rg;
	
	@NotNull(message = "Não foi selecionada uma residência de destino!")
	private Long residenciaId;
	
	private String placa;
	
	private VeiculoVisitaDto veiculoVisita;

}
