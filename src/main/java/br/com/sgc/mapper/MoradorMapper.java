package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.dto.MoradorDto;
import br.com.sgc.entities.Morador;

@Mapper(componentModel = "spring")
public abstract class MoradorMapper {
	
	public abstract Morador moradorDtoToMorador(MoradorDto dto);
	
	public abstract MoradorDto moradorToMoradorDto(Morador morador);

}
