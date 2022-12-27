package br.com.sgc.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitaService<T, X> {
	
	/**
	 * Busca visitas
	 * 
	 * @param filter
	 * @param pageable
	 */
	Page<T> buscarVisitas(X filter, Pageable pageable);

}
