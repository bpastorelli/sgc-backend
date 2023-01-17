package br.com.sgc.repositories.queries.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.sgc.access.entities.AcessoModulo;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.AcessoModuloFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class AcessoModuloQueryRepositoryImpl implements QueryRepository<AcessoModulo, AcessoModuloFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<AcessoModulo> entity_;
	private static Join<AcessoModulo, Modulo> moduloJoin_;
	private CriteriaBuilder builder;
	private CriteriaQuery<AcessoModulo> query;
	
	@Override
	public List<AcessoModulo> query(AcessoModuloFilter filters, Pageable pageable) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(AcessoModulo.class);
		
        entity_ = query.from(AcessoModulo.class);
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<AcessoModulo> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
		
	}

	@Override
	public List<AcessoModulo> query(AcessoModuloFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(AcessoModulo.class);
		
        entity_ = query.from(AcessoModulo.class);
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<AcessoModulo> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
	}

	@Override
	public Predicate[] criarFiltros(Root<AcessoModulo> root, AcessoModuloFilter filters,
			CriteriaBuilder builder) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getIdUsuario() != null)
			predicates.add(builder.equal(root.get("idUsuario"), filters.getIdUsuario()));
		
		if(filters.getIdModulo() != null)
			predicates.add(builder.equal(moduloJoin_.get("id"), filters.getIdModulo()));		
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		predicates.add(builder.equal(root.get("acesso"), filters.isAcesso()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
		
	}

	@Override
	public long totalRegistros(AcessoModuloFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<AcessoModulo> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

}
