package br.com.sgc.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETResidenciaResponseDto implements Comparable<GETResidenciaResponseDto> {
	
	private Long   id;
	
	private String endereco;
	
	private Long   numero;
	
	private String complemento;
	
	private String bairro;
	
	private String cep;
	
	private String cidade;
	
	private String uf;
	
	private List<GETMoradorSemResidenciasResponseDto> moradores;

	private String guide;

	@Override
	public int compareTo(GETResidenciaResponseDto o) {
		// TODO Auto-generated method stub
		return this.endereco.compareTo(o.endereco);
	}
}
