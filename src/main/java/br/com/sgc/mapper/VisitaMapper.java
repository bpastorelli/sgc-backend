package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.VisitaAvro;
import br.com.sgc.dto.VisitaDto;

@Mapper(componentModel = "spring")
public abstract class VisitaMapper {
	
	public abstract VeiculoAvro veiculoDtoToVeiculoAvro(VisitaDto dto);
	
	public abstract VisitaAvro visitaDtoToVisitaAvro(VisitaDto dto); 
}
