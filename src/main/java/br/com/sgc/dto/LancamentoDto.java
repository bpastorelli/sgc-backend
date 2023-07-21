package br.com.sgc.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.sgc.entities.Morador;
import br.com.sgc.entities.Residencia;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LancamentoDto {
	
	private Morador morador;
	
	private LocalDateTime dataPagamento;
	
	private String documento;
	
	private String periodo;
	
	private BigDecimal valor;
	
	private Residencia residencia;
	
	private String requisicaoId;

}
