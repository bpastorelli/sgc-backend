package br.com.sgc.dto;

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
public class VinculoResidenciaDto {
	
	private Long residenciaId;
	
	private Long moradorId;
	
	private String guide;

}
