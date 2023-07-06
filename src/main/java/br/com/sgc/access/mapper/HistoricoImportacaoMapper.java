package br.com.sgc.access.mapper;

import org.mapstruct.Mapper;

import br.com.sgc.access.dto.HistoricoImportacaoDto;
import br.com.sgc.entities.HistoricoImportacao;

@Mapper(componentModel = "spring")
public interface HistoricoImportacaoMapper {
	
	public abstract HistoricoImportacao toHistoricoImportacao(HistoricoImportacaoDto dto);

}
