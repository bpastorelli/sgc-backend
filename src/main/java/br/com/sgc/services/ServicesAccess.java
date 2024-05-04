package br.com.sgc.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.sgc.errorheadling.RegistroException;

/**
 * @author bodl
 *
 * @param <POST> - Post DTO 
 * @param <PUT> - Put DTO
 * @param <GET> - Get DTO
 * @param <Filter> - Filtros
 */
public interface ServicesAccess<POST, PUT, GET, Filter> {

	abstract GET cadastra(POST post) throws RegistroException;
	
	abstract List<GET> cadastraEmLote(List<POST> post) throws RegistroException;
	
	abstract GET atualiza(PUT put, Long id) throws RegistroException;
	
	abstract List<GET> atualizaEmLote(List<PUT> put, Long id) throws RegistroException;
	
	abstract Page<GET> buscaPaginado(Filter filter, Pageable pageable) throws RegistroException;
	
	abstract GET busca(Filter filter, Pageable pageable) throws RegistroException;
	
}
