package br.com.sgc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

@Data
public class ContribuicaoDto {
	
	@CPF(message = "CPF inválido!")
	private String cpf;
	
	@NotEmpty(message = "O campo data pagamento é obrigatório.")
	private LocalDate dataPagamento;
	
	private String documento;
	
	@NotEmpty(message = "O campo valor é obrigatório.")
	private BigDecimal valor;
	
	@NotEmpty(message = "O campo residencia é obrigatório.")
	private Long residenciaId;
	
	private String guide;
	

}
