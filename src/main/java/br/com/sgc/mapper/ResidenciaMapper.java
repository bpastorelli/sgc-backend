package br.com.sgc.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.sgc.ResidenciaAvro;
import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;

@Mapper(componentModel = "spring")
public abstract class ResidenciaMapper {
	
	public abstract Residencia residenciaDtoToResiencia(ResidenciaDto dto);
	
	public abstract ResidenciaDto residenciaToResidenciaDto(Residencia residencia);
	
	public abstract ResidenciaDto atualizaResidenciaDtoToResidenciaDto(AtualizaResidenciaDto dto);
	
	public abstract GETResidenciaResponseDto residenciaToGETResidenciaResponseDto(Residencia residencia);
	
	public abstract ResidenciaAvro residenciaDtoToResidenciaAvro(ResidenciaDto dto);
	
	public abstract List<Residencia> listResidenciaDtoToListResidencia(List<ResidenciaDto> dtos);

	public abstract List<ResidenciaDto> listResidenciaToListResidenciaDto(List<Residencia> residencias);
	
	public abstract List<GETResidenciaResponseDto> listResidenciaToListGETResidenciaResponseDto(List<Residencia> residencias);
}
