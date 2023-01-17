package br.com.sgc.access.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.access.dto.GETAcessoModuloResponseDto;
import br.com.sgc.access.entities.AcessoModulo;

@Mapper(componentModel = "spring")
public interface AcessoModuloMapper {
	
	@Mapping(target = "idModulo", source = "modulo.id")
	@Mapping(target = "nomeModulo", source = "modulo.descricao")
	@Mapping(target = "pathModulo", source = "modulo.pathModulo")
	public abstract GETAcessoModuloResponseDto acessoModuloToGETAcessoModuloResponseDto(AcessoModulo entidade);

	@Mapping(target = "idModulo", source = "modulo.id")
	@Mapping(target = "nomeModulo", source = "modulo.descricao")
	@Mapping(target = "pathModulo", source = "modulo.pathModulo")
	public abstract List<GETAcessoModuloResponseDto> listAcessoModuloToListGETAcessoModuloResponseDto(List<AcessoModulo> entidade);

}
