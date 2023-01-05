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

import br.com.sgc.entities.Veiculo;
import br.com.sgc.filter.VeiculoFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class VeiculoQueryRepositoryImpl implements QueryRepository<Veiculo, VeiculoFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<Veiculo> entity_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Veiculo> query;
	
	@Override
	public List<Veiculo> query(VeiculoFilter filters, Pageable pageable) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Veiculo.class);
		
        entity_ = query.from(Veiculo.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Veiculo> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
		
	}

	@Override
	public Predicate[] criarFiltros(Root<Veiculo> root, VeiculoFilter filters, CriteriaBuilder builder) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));

		if(filters.getPlaca() != null)
			predicates.add(builder.like(root.get("placa"), filters.getPlaca().replace("-", "")));
		
		if(filters.getMarca() != null)
			predicates.add(builder.like(root.get("marca"), filters.getMarca() + '%'));
		
		if(filters.getModelo() != null)
			predicates.add(builder.like(root.get("modelo"), filters.getModelo() + '%'));
		
		if(filters.getCor() != null)
			predicates.add(builder.like(root.get("cor"), filters.getCor() + '%'));
			
		if(filters.getAno() != null)
			predicates.add(builder.equal(root.get("ano"), filters.getAno()));
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		if(filters.getGuide() != null)
			predicates.add(builder.equal(root.get("guide"), filters.getGuide()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
		
	}

	@Override
	public long totalRegistros(VeiculoFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("total");
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Veiculo> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

}
