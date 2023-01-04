package br.com.sgc.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.sgc.MoradorAvro;
import br.com.sgc.ProcessoCadastroAvro;
import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.entities.Morador;

@Mapper(componentModel = "spring")
public abstract class MoradorMapper {
	
	public abstract Morador moradorDtoToMorador(MoradorDto dto);
	
	public abstract MoradorDto moradorToMoradorDto(Morador morador);
	
	public abstract List<GETMoradorResponseDto> listMoradorToListGETMoradorResponseDto(List<Morador> moradores);
	
	public abstract List<Morador> listMoradorDtoToListMorador(List<MoradorDto> moradoresDto);
	
	public abstract MoradorAvro moradorDtoToMoradorAvro(MoradorDto dto);
	
	public abstract MoradorAvro atualizaMoradorDtoToMoradorAvro(AtualizaMoradorDto dto);
	
	public abstract ProcessoCadastroAvro processoDtoToProcessoAvro(ProcessoCadastroDto dto);
	
	public abstract GETMoradorResponseDto moradorToGETMoradorResponseDto(Morador morador);
	
	public abstract MoradorDto atualizaMoradorDtoToMoradorDto(AtualizaMoradorDto dto);

}
