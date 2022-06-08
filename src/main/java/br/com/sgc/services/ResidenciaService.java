package br.com.sgc.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.sgc.dto.GETResidenciaResponseDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.ResidenciaFilter;
import br.com.sgc.response.Response;

public interface ResidenciaService<T> {

	/**
	 * Cadastra uma lista de residencias na base de dados.
	 * 
	 * @param lista de residencias
	 * @return Response<T>
	 */
	Response<List<T>> persistir(List<T> residencias) throws RegistroException;
	
	/**
	 * Cadastra uma residencia
	 * 
	 * @param residencia
	 * @return Response<T>
	 */
	Response<T> persistir(T residencia) throws RegistroException;
	
	/**
	 * Busca residencias
	 * 
	 * @param filter
	 * @param pageable
	 * @return Page<Residencia>
	 */
	Page<GETResidenciaResponseDto> buscarResidencia(ResidenciaFilter filter, Pageable pageable);
	
	/**
	 * Busca registro pelo ticket de envio
	 * @param guide 
	 * @return Response<T>
	 */
	Response<T> buscarPorGuide(String guide);

	
}
