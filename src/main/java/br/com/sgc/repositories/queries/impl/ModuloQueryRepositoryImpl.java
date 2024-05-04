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

import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.ModuloFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class ModuloQueryRepositoryImpl implements QueryRepository<Modulo, ModuloFilter> {
	
	@PersistenceContext
	private EntityManager manager;
	private static Root<Modulo> entity_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Modulo> query;
	
	
	@Override
	public List<Modulo> query(ModuloFilter filters, Pageable pageable){
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Modulo.class);
		
        entity_ = query.from(Modulo.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Modulo> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
	}
	
	@Override
	public Predicate[] criarFiltros(Root<Modulo> root, ModuloFilter filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getDescricao() != null)
			predicates.add(builder.like(root.get("descricao"), filters.getDescricao() + '%'));
		
		if(filters.getPathModulo() != null)
			predicates.add(builder.equal(root.get("pathModulo"), filters.getPathModulo()));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(ModuloFilter filters) {
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Modulo> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

	@Override
	public List<Modulo> query(ModuloFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Modulo.class);
		
        entity_ = query.from(Modulo.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Modulo> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
		
	}
	
}
