package br.com.sgc.access.dto;

import java.util.List;

import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.Modulo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroAcessoModuloResponseDto {
	
	private List<Modulo> modulos;
	
	private List<AcessoFuncionalidade> funcionalidades;

}
