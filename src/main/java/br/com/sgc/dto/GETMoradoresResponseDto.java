package br.com.sgc.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GETMoradoresResponseDto {

	public List<GETMoradorResponseDto> moradores;
	
}
