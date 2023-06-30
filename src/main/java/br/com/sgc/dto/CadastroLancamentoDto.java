package br.com.sgc.dto;

import java.util.List;

import javax.validation.constraints.Size;

import br.com.sgc.entities.Lancamento;
import lombok.Data;

@Data
public class CadastroLancamentoDto {
	
	@Size(min = 1, message = "Você deve incluir ao menos um lançamento.")
	private List<Lancamento> lancamentos;

}
