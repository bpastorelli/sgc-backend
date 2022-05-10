package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.dto.VeiculoDto;

@Mapper(componentModel = "spring")
public abstract class VeiculoMapper {
	
	public abstract VeiculoAvro veiculoDtoToVeiculoAvro(VeiculoDto dto);

}
