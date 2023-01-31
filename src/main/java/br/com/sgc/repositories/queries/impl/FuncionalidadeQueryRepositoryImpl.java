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

import br.com.sgc.access.entities.Funcionalidade;
import br.com.sgc.access.filter.FuncionalidadeFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class FuncionalidadeQueryRepositoryImpl implements QueryRepository<Funcionalidade, FuncionalidadeFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<Funcionalidade> entity_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Funcionalidade> query;
	
	@Override
	public List<Funcionalidade> query(FuncionalidadeFilter filters, Pageable pageable) {
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Funcionalidade.class);
		
        entity_ = query.from(Funcionalidade.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Funcionalidade> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
	}

	@Override
	public List<Funcionalidade> query(FuncionalidadeFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Funcionalidade.class);
		
        entity_ = query.from(Funcionalidade.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Funcionalidade> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
	}

	@Override
	public Predicate[] criarFiltros(Root<Funcionalidade> root, FuncionalidadeFilter filters, CriteriaBuilder builder) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getIdModulo() != null)
			predicates.add(builder.equal(root.get("idModulo"), filters.getIdModulo()));
		
		if(filters.getDescricao() != null)
			predicates.add(builder.like(root.get("descricao"), filters.getDescricao() + '%'));
		
		if(filters.getPathFuncionalidade() != null)
			predicates.add(builder.equal(root.get("pathFuncionalidade"), filters.getPathFuncionalidade()));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
		
	}

	@Override
	public long totalRegistros(FuncionalidadeFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Funcionalidade> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

}
