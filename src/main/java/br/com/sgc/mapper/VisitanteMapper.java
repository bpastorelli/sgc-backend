package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VisitanteAvro;
import br.com.sgc.dto.AtualizaVisitanteDto;
import br.com.sgc.dto.GETVisitanteResponseDto;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.entities.Visitante;

@Mapper(componentModel = "spring")
public abstract class VisitanteMapper {
	
	public abstract VisitanteAvro visitanteDtoToVisitanteAvro(VisitanteDto dto); 
	
	public abstract VisitanteDto visitanteToVisitanteDto(Visitante visitante);
	
	public abstract VisitanteDto atualizaVisitanteDtoToVisitanteDto(AtualizaVisitanteDto dto);
	
	public abstract GETVisitanteResponseDto visitanteToGETVisitanteResponseDto(Visitante visitante);
}
