package br.com.sgc.access.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.sgc.access.dto.CadastroFuncionalidadeDto;
import br.com.sgc.access.dto.GETFuncionalidadeResponseDto;
import br.com.sgc.access.entities.Funcionalidade;

@Mapper(componentModel = "spring")
public interface FuncionalidadeMapper {
	
	public abstract CadastroFuncionalidadeDto funcionalidadeToCadastroFuncionalidadeDto(Funcionalidade entidade);
	
	public abstract Funcionalidade cadastroFuncionalidadeDtoToFuncionalidade(CadastroFuncionalidadeDto dto);
	
	public abstract GETFuncionalidadeResponseDto funcionalidadeToGETFuncionalidadeResponseDto(Funcionalidade modulo);
	
	public abstract List<Funcionalidade> listCadastroFuncionalidadeDtoToListFuncionalidade(List<CadastroFuncionalidadeDto> dto);

}
