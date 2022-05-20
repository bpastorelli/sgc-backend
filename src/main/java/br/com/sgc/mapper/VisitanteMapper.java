package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VisitanteAvro;
import br.com.sgc.dto.VisitanteDto;

@Mapper(componentModel = "spring")
public abstract class VisitanteMapper {
	
	public abstract VisitanteAvro visitanteDtoToVisitanteAvro(VisitanteDto dto); 
}
