package br.com.sgc.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Services<T, F> {
	
	Page<T> buscar(F filter, Pageable pageable);

}
