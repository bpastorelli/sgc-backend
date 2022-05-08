package br.com.sgc.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VisitaDto {
	
	private String guide;
	
	@NotNull(message = "Campo RG é obrigatório!")
	private String rg;
	
	private String cpf;
	
	@NotNull(message = "Não foi selecionada uma residência de destino!")
	private Long residenciaId;
	
	private String placa;
	
	private VeiculoVisitaDto veiculo;

}
