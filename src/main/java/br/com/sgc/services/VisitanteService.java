package br.com.sgc.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.sgc.filter.VisitanteFilter;
import br.com.sgc.response.Response;

public interface VisitanteService<T> {
	
	/**
	 * Busca visitantes
	 * 
	 * @param id
	 * @param cpf
	 * @param rg
	 * @param nome
	 * @param email
	 * @param pageRequest
	 * @return Page<Morador>
	 */
	Page<T> buscarVisitante(VisitanteFilter filter, Pageable pageable);
	
	/**
	 * Busca registro pelo ticket de envio
	 * @param guide 
	 * @return Response<T>
	 */
	Response<T> buscarPorGuide(String guide);

	
}
