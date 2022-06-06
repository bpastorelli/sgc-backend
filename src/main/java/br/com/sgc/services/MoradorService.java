package br.com.sgc.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.sgc.dto.GETMoradorResponseDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.response.Response;

public interface MoradorService<T> {

	/**
	 * Cadastra uma lista de moradores na base de dados.
	 * 
	 * @param morador
	 * @return Response<T>
	 */
	Response<List<GETMoradorResponseDto>> persistir(List<T> moradores) throws RegistroException;
	
	/**
	 * Cadastra um morador
	 * 
	 * @param morador
	 * @return Response<T>
	 */
	Response<T> persistir(T morador) throws RegistroException;
	
	/**
	 * Busca moradores
	 * 
	 * @param id
	 * @param cpf
	 * @param rg
	 * @param nome
	 * @param email
	 * @param pageRequest
	 * @return Page<Morador>
	 */
	Page<GETMoradorResponseDto> buscarMorador(MoradorFilter filter, Pageable pageable);
	
	/**
	 * Busca registro pelo ticket de envio
	 * @param guide 
	 * @return Response<T>
	 */
	Response<T> buscarPorGuide(String guide);

	
}
