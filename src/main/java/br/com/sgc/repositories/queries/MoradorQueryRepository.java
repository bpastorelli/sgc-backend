package br.com.sgc.repositories.queries;
	
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;

import br.com.sgc.filter.MoradorFilter;

public interface MoradorQueryRepository<T> {
	
	public List<T> findMoradorBy(MoradorFilter filters, Pageable pageable);
	
	public Predicate[] criarFiltros(Root<T> root, MoradorFilter filters, CriteriaBuilder builder);
	
	public long totalRegistros(MoradorFilter filters);
	
	public void aplicarFiltroPaginacao(TypedQuery<T> typedQuery, Pageable pageable);
}
