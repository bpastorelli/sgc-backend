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

import br.com.sgc.access.entities.AcessoFuncionalidade;
import br.com.sgc.access.entities.Funcionalidade;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.filter.AcessoFuncionalidadeFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class AcessoFuncionalidadeQueryRepositoryImpl implements QueryRepository<AcessoFuncionalidade, AcessoFuncionalidadeFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<AcessoFuncionalidade> entity_;
	private static Join<AcessoFuncionalidade, Modulo> moduloJoin_;
	private static Join<AcessoFuncionalidade, Funcionalidade> funcionalidadeJoin_;
	private CriteriaBuilder builder;
	private CriteriaQuery<AcessoFuncionalidade> query;
	
	@Override
	public List<AcessoFuncionalidade> query(AcessoFuncionalidadeFilter filters, Pageable pageable) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(AcessoFuncionalidade.class);
		
        entity_ = query.from(AcessoFuncionalidade.class);
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
        funcionalidadeJoin_ = entity_.join("funcionalidade", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<AcessoFuncionalidade> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
		
	}

	@Override
	public List<AcessoFuncionalidade> query(AcessoFuncionalidadeFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(AcessoFuncionalidade.class);
		
        entity_ = query.from(AcessoFuncionalidade.class);
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
        funcionalidadeJoin_ = entity_.join("funcionalidade", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<AcessoFuncionalidade> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
	}

	@Override
	public Predicate[] criarFiltros(Root<AcessoFuncionalidade> root, AcessoFuncionalidadeFilter filters,
			CriteriaBuilder builder) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getIdUsuario() != null)
			predicates.add(builder.equal(root.get("idUsuario"), filters.getIdUsuario()));
		
		if(filters.getIdModulo() != null)
			predicates.add(builder.and(moduloJoin_.get("id").in(filters.getIdModulo())));
		
		if(filters.getIdFuncionalidade() != null)
			predicates.add(builder.and(funcionalidadeJoin_.get("id").in(filters.getIdFuncionalidade())));		
			
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		predicates.add(builder.equal(root.get("acesso"), filters.isAcesso()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
		
	}

	@Override
	public long totalRegistros(AcessoFuncionalidadeFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
        moduloJoin_ = entity_.join("modulo", JoinType.INNER);
        funcionalidadeJoin_ = entity_.join("funcionalidade", JoinType.INNER);
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<AcessoFuncionalidade> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

}
