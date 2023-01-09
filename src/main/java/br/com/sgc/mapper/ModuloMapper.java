package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.access.dto.CadastroModuloDto;
import br.com.sgc.access.dto.GETModuloResponseDto;
import br.com.sgc.access.entities.Modulo;

@Mapper(componentModel = "spring")
public interface ModuloMapper {
	
	public abstract CadastroModuloDto moduloToCadastroModuloDto(Modulo entidade);
	
	public abstract Modulo cadastroModuloDtoToModulo(CadastroModuloDto dto);
	
	public abstract GETModuloResponseDto moduloToGETModuloResponseDto(Modulo modulo);

}
