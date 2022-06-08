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

import br.com.sgc.entities.Visitante;
import br.com.sgc.filter.VisitanteFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class VisitanteQueryRepositoryImpl implements QueryRepository<Visitante, VisitanteFilter> {
	
	@PersistenceContext
	private EntityManager manager;
	private static Root<Visitante> entity_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Visitante> query;
	
	
	@Override
	public List<Visitante> query(VisitanteFilter filters, Pageable pageable){
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Visitante.class);
		
        entity_ = query.from(Visitante.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Visitante> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
	}
	
	@Override
	public Predicate[] criarFiltros(Root<Visitante> root, VisitanteFilter filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getNome() != null)
			predicates.add(builder.like(root.get("nome"), filters.getNome() + '%'));
		
		if(filters.getCpf() != null)
			predicates.add(builder.equal(root.get("cpf"), filters.getCpf()));
		
		if(filters.getRg() != null)
			predicates.add(builder.equal(root.get("rg"), filters.getRg()));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(VisitanteFilter filters) {
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Visitante> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}
	
}
