package br.com.sgc.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.sgc.MoradorAvro;
import br.com.sgc.ProcessoCadastroAvro;
import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.GETMoradorSemResidenciasResponseDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.entities.Morador;

@Mapper(componentModel = "spring")
public interface MoradorMapper {
	
	public abstract Morador moradorDtoToMorador(MoradorDto dto);
	
	public abstract MoradorDto moradorToMoradorDto(Morador morador);
	
	public abstract List<GETMoradorResponseDto> listMoradorToListGETMoradorResponseDto(List<Morador> moradores);
	
	public abstract List<Morador> listMoradorDtoToListMorador(List<MoradorDto> moradoresDto);
	
	public abstract MoradorAvro moradorDtoToMoradorAvro(MoradorDto dto);
	
	public abstract MoradorAvro atualizaMoradorDtoToMoradorAvro(AtualizaMoradorDto dto);
	
	public abstract ProcessoCadastroAvro processoDtoToProcessoAvro(ProcessoCadastroDto dto);
	
	@Mapping(source = "nome", target = "nome", qualifiedByName = "ToUpperCase")
	public abstract GETMoradorResponseDto moradorToGETMoradorResponseDto(Morador morador);
	
	@Mapping(source = "nome", target = "nome", qualifiedByName = "ToUpperCase")
	public abstract GETMoradorSemResidenciasResponseDto moradorToGETMoradorSemResidenciasResponseDto(Morador morador);
	
	public abstract MoradorDto atualizaMoradorDtoToMoradorDto(AtualizaMoradorDto dto);
	
	@Named("ToUpperCase")
	default String toUpperCase(String value) {
		
		return value.toUpperCase();
		
	}

}
