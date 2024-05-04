package br.com.sgc.access.filter;

import java.util.List;

import br.com.sgc.enums.FuncaoFuncionalidadeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionalidadeFilter {

	private Long id;
	
	private List<Long> idModulo;
	
	private String descricao;
	
	private FuncaoFuncionalidadeEnum funcao;
	
	private String pathFuncionalidade;
	
	private Long posicao;
	
	private boolean content;
	
}
