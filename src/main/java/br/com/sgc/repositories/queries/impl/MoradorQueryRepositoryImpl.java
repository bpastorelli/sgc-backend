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

import br.com.sgc.entities.Morador;
import br.com.sgc.entities.Residencia;
import br.com.sgc.entities.VinculoResidencia;
import br.com.sgc.filter.MoradorFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class MoradorQueryRepositoryImpl implements QueryRepository<Morador, MoradorFilter> {
	
	@PersistenceContext
	private EntityManager manager;
	private static Root<Morador> entity_;
	private static Join<Morador, VinculoResidencia> vinculoJoin_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Morador> query;
	
	
	@Override
	public List<Morador> query(MoradorFilter filters, Pageable pageable){
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Morador.class);
		
        entity_ = query.from(Morador.class);
        vinculoJoin_ = entity_.join("residencias", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        this.query.orderBy(builder.asc(entity_.get("nome")));
        
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
		
		if(filters.getCpf() != null)
			predicates.add(builder.equal(root.get("cpf"), filters.getCpf()));
		
		if(filters.getRg() != null)
			predicates.add(builder.equal(root.get("rg"), filters.getRg()));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		if(filters.getEmail() != null)
			predicates.add(builder.equal(root.get("email"), filters.getEmail()));
		
		if(filters.getGuide() != null)
			predicates.add(builder.like(root.get("guide"), filters.getGuide() + '%'));
		
		
		Residencia residencia = new Residencia();
		residencia.setId(filters.getResidenciaId());
		if(filters.getResidenciaId() != null)
			predicates.add(builder.equal(vinculoJoin_.get("residencia"), residencia));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(MoradorFilter filters) {
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		vinculoJoin_ = entity_.join("residencias", JoinType.INNER);
		entity_.alias("total"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Morador> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

	@Override
	public List<Morador> query(MoradorFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Morador.class);
		
        entity_ = query.from(Morador.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Morador> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
		
	}
	
}
