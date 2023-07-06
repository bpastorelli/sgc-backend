package br.com.sgc.access.dto;


import br.com.sgc.enums.SituacaoEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HistoricoImportacaoDto {
	
	private String nomeArquivo;
	
	private SituacaoEnum situacao;
	
}
