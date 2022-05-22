package br.com.sgc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.VisitaAvro;
import br.com.sgc.dto.VisitaDto;

@Mapper(componentModel = "spring")
public abstract class VisitaMapper {
	
	public abstract VeiculoAvro veiculoDtoToVeiculoAvro(VisitaDto dto);
	
	@Mapping(target = "veiculoVisita.marca", source = "veiculoVisita.marca")
	@Mapping(target = "veiculoVisita.modelo", source = "veiculoVisita.modelo")
	@Mapping(target = "veiculoVisita.cor", source = "veiculoVisita.cor")
	@Mapping(target = "veiculoVisita.ano", source = "veiculoVisita.ano")
	public abstract VisitaAvro visitaDtoToVisitaAvro(VisitaDto dto); 
}
