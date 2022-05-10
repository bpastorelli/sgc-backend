package br.com.sgc.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.sgc.ResidenciaAvro;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;

@Mapper(componentModel = "spring")
public abstract class ResidenciaMapper {
	
	public abstract Residencia residenciaDtoToResiencia(ResidenciaDto dto);
	
	public abstract ResidenciaDto residenciaToResidenciaDto(Residencia residencia);
	
	public abstract ResidenciaAvro residenciaDtoTpResidenciaAvro(ResidenciaDto dto);
	
	public abstract List<Residencia> listResidenciaDtoToListResidencia(List<ResidenciaDto> dtos);

	public abstract List<ResidenciaDto> listResidenciaToListResidenciaDto(List<Residencia> residencias);
}
