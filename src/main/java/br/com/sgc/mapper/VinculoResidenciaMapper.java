package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VinculoResidenciaAvro;
import br.com.sgc.dto.VinculoResidenciaDto;

@Mapper(componentModel = "spring")
public abstract class VinculoResidenciaMapper {
	
	public abstract VinculoResidenciaAvro vinculoResidenciaDtoToVinculoResidenciaAvro(VinculoResidenciaDto dto);

}
