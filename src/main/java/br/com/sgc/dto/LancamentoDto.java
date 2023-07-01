package br.com.sgc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

@Data
public class LancamentoDto {
	
	@CPF(message = "CPF inválido!")
	private String cpf;
	
	@NotEmpty(message = "O campo data pagamento é obrigatório.")
	private LocalDateTime dataPagamento;
	
	private String documento;
	
	@NotEmpty(message = "O campo valor é obrigatório.")
	private BigDecimal valor;
	
	@NotEmpty(message = "O campo residencia é obrigatório.")
	private Long residenciaId;
	

}
