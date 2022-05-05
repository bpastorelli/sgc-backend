package br.com.sgc.repositories.queries.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.sgc.commons.CriteriaSubQuery;
import br.com.sgc.entities.Morador;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.repositories.queries.MoradorQueryRepository;

@Repository
public class MoradorQueryRepositoryImpl implements MoradorQueryRepository<Morador> {
	
	@PersistenceContext
	private EntityManager manager;
	private CriteriaBuilder builder;
	private CriteriaQuery<Morador> query;
	
	
	@Override
	public List<Morador> findMoradorBy(MoradorFilter filters, Pageable pageable){
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Morador.class);
		
        Root<Morador> root = query.from(Morador.class);
        
        query.where(this.criarFiltros(root, filters, builder));
        
        TypedQuery<Morador> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
	}
	
	@Override
	public Predicate[] criarFiltros(Root<Morador> root, MoradorFilter filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getNome() != null)
			predicates.add(builder.like(root.get("nome"), filters.getNome() + '%'));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(MoradorFilter filters) {
		
		Root<Morador> entity_ = null;
		
		return CriteriaSubQuery.subQueryTotal(manager, entity_, builder, query, this.criarFiltros(entity_, filters, builder));
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Morador> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}
	
	

}
