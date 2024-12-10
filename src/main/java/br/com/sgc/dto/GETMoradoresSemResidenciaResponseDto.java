package br.com.sgc.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GETMoradoresSemResidenciaResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<GETMoradorSemResidenciasResponseDto> moradores;

}
