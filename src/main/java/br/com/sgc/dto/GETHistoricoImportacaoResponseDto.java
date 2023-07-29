package br.com.sgc.dto;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.sgc.enums.SituacaoEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETHistoricoImportacaoResponseDto {

	private Long id;
	
	private String idRequisicao;
	
	private LocalDate dataCriacao;
	
	private String nomeArquivo;
	
	@Enumerated(EnumType.STRING)
	private SituacaoEnum situacao;
	
}
