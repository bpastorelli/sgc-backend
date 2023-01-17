package br.com.sgc.access.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.access.dto.GETAcessoFuncionalidadeResponseDto;
import br.com.sgc.access.entities.AcessoFuncionalidade;

@Mapper(componentModel = "spring")
public interface AcessoFuncionalidadeMapper {
	
	@Mapping(target = "idModulo", source = "modulo.id")
	@Mapping(target = "nomeModulo", source = "modulo.descricao")
	@Mapping(target = "idFuncionalidade", source = "funcionalidade.id")
	@Mapping(target = "nomeFuncionalidade", source = "funcionalidade.descricao")
	@Mapping(target = "pathFuncionalidade", source = "funcionalidade.pathFuncionalidade")
	public abstract GETAcessoFuncionalidadeResponseDto acessoFuncionalidadeToGETAcessoFuncionalidadeResponseDto(AcessoFuncionalidade entidade);

	@Mapping(target = "idModulo", source = "modulo.id")
	@Mapping(target = "nomeModulo", source = "modulo.descricao")
	@Mapping(target = "idFuncionalidade", source = "funcionalidade.id")
	@Mapping(target = "nomeFuncionalidade", source = "funcionalidade.descricao")
	@Mapping(target = "pathFuncionalidade", source = "funcionalidade.pathFuncionalidade")
	public abstract List<GETAcessoFuncionalidadeResponseDto> listAcessoFuncionalidadeToListGETAcessoFuncionalidadeResponseDto(List<AcessoFuncionalidade> entidade);

}
