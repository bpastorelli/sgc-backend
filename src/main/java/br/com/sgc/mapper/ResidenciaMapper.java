package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.ResidenciaAvro;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;

@Mapper(componentModel = "spring")
public abstract class ResidenciaMapper {
	
	public abstract Residencia residenciaDtoToResiencia(ResidenciaDto dto);
	
	public abstract ResidenciaDto residenciaToResienciaDto(Residencia residencia);
	
	public abstract ResidenciaAvro residenciaDtoTpResidenciaAvro(ResidenciaDto dto);

}
