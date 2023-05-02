package br.com.sgc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.sgc.VeiculoAvro;
import br.com.sgc.dto.AtualizaVeiculoDto;
import br.com.sgc.dto.GETVeiculoResponseDto;
import br.com.sgc.dto.GETVeiculoSemVisitantesResponseDto;
import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.entities.Veiculo;
import br.com.sgc.utils.Utils;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
	
	public abstract VeiculoAvro veiculoDtoToVeiculoAvro(VeiculoDto dto);
	
	@Mapping(target = "placa", source = "placa", qualifiedByName = "FormatarPlaca")
	public abstract GETVeiculoResponseDto veiculoToGETVeiculoResponseDto(Veiculo veiculo);
	
	public abstract VeiculoAvro atualizaVeiculoDtoToVeiculoAvro(AtualizaVeiculoDto dto);
	
	public abstract VeiculoDto atualizaVeiculoDtoToVeiculoDto(AtualizaVeiculoDto dto);
	
	public abstract VeiculoDto veiculoToVeiculoDto(Veiculo veiculo);
	
	public abstract GETVeiculoSemVisitantesResponseDto veiculoToGETVeiculoSemVisitantesResponseDto(Veiculo veiculo);
	
	@Named("FormatarPlaca")
	default String formatarPlaca(String placa) {
		
		return Utils.formatPlaca(placa);
		
	}

}
