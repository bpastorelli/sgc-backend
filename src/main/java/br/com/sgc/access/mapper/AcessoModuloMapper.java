package br.com.sgc.access.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.sgc.access.dto.GETAcessoModuloResponseDto;
import br.com.sgc.access.entities.AcessoModulo;
import br.com.sgc.access.entities.Modulo;

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
	
	@Mapping(target = "idModulo", source = "id")
	@Mapping(target = "nomeModulo", source = "descricao")
	@Mapping(target = "pathModulo", source = "pathModulo")
	public abstract GETAcessoModuloResponseDto moduloToGETAcessoModuloResponseDto(Modulo modulos);
	
	@Mapping(target = "idModulo", source = "id")
	@Mapping(target = "nomeModulo", source = "descricao")
	@Mapping(target = "pathModulo", source = "pathModulo")
	public abstract List<GETAcessoModuloResponseDto> listModuloToListGETAcessoModuloResponseDto(List<Modulo> modulos);
	
	@Named("ToBoolean")
	default boolean toUpperCase(boolean value) {
		
		return !value ? false : value;
		
	}
	
	
	
}
