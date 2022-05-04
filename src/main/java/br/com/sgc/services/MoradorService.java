package br.com.sgc.services;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.sgc.dto.MoradorDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.response.Response;

public interface MoradorService {

	/**
	 * Cadastra uma lista de moradores na base de dados.
	 * 
	 * @param morador
	 * @return Response<T>
	 */
	Response<List<MoradorDto>> persistir(List<MoradorDto> moradores) throws RegistroException;
	
	/**
	 * Cadastra um morador
	 * 
	 * @param morador
	 * @return Response<T>
	 */
	Response<?> persistir(MoradorDto morador);
	
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
	Page<?> buscarMorador(Predicate predicate, PageRequest pageRequest);
	
	/**
	 * Busca registro pelo ticket de envio
	 * @param guide 
	 * @return Response<T>
	 */
	Response<MoradorDto> buscarPorGuide(String guide);

	
}
