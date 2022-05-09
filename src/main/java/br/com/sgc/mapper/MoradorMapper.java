package br.com.sgc.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.sgc.MoradorAvro;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.entities.Morador;

@Mapper(componentModel = "spring")
public abstract class MoradorMapper {
	
	public abstract Morador moradorDtoToMorador(MoradorDto dto);
	
	public abstract MoradorDto moradorToMoradorDto(Morador morador);

	public abstract List<MoradorDto> listMoradorToListMoradorDto(List<Morador> moradores);
	
	public abstract List<Morador> listMoradorDtoToListMorador(List<MoradorDto> moradoresDto);
	
	public abstract MoradorAvro moradorDtoToMoradorAvro(MoradorDto dto);

}
