package br.com.sgc.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.entities.Veiculo;

@Mapper(componentModel = "spring")
public abstract class VeiculoMapper {
	
	public abstract VeiculoAvro veiculoDtoToVeiculoAvro(VeiculoDto dto);
	
	public abstract GETVeiculoResponseDto veiculoToGETVeiculoResponseDto(Veiculo veiculo);

}
