package br.com.sgc.repositories.queries.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import br.com.sgc.entities.Lancamento;
import br.com.sgc.entities.Morador;
import br.com.sgc.filter.ContribuicaoFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class ContribuicaoQueryRepositoryImpl implements QueryRepository<Lancamento, ContribuicaoFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<Lancamento> entity_;
	private static Join<Lancamento, Morador> moradorJoin_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Lancamento> query;
	
	@Override
	public List<Lancamento> query(ContribuicaoFilter filters, Pageable pageable) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Lancamento.class);
		
        entity_ = query.from(Lancamento.class);
        moradorJoin_ = entity_.join("morador", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        this.query.orderBy(builder.asc(moradorJoin_.get("nome")));
        
        TypedQuery<Lancamento> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
		
	}

	@Override
	public Predicate[] criarFiltros(Root<Lancamento> root, ContribuicaoFilter filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));

		if(filters.getMoradorId() != null)
			predicates.add(builder.equal(moradorJoin_.get("id"), filters.getMoradorId()));
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getNome() != null)
			predicates.add(builder.like(moradorJoin_.get("nome"), filters.getNome() + '%'));
		
		if(filters.getRg() != null)
			predicates.add(builder.equal(moradorJoin_.get("rg"), filters.getRg()));
		
		if(filters.getCpf() != null)
			predicates.add(builder.equal(moradorJoin_.get("cpf"), filters.getCpf()));
		
		if(filters.getDataInicio() != null && filters.getDataFim() != null)
			predicates.add(builder.between(root.get("dataPagamento"), ajustaData(filters.getDataInicio(), true), ajustaData(filters.getDataFim(), false)));
		
		if(filters.getDataInicio() != null && filters.getDataFim() == null)
			predicates.add(builder.equal(root.get("dataPagamento"), ajustaData(filters.getDataInicio(), true)));
			
		if(filters.getGuide() != null)
			predicates.add(builder.equal(root.get("guide"), filters.getGuide()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(ContribuicaoFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		moradorJoin_ = entity_.join("morador", JoinType.INNER);
		entity_.alias("total"); 
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Lancamento> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}
	
	public LocalDateTime ajustaData(LocalDate date, boolean startDate) {
		
		LocalDateTime newDate;
				
		if(startDate) {
			LocalDateTime date1 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
			newDate = date1;
		}else {
			LocalDateTime date2 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 59, 59);
			newDate = date2;
		}
		
		return newDate;
		
	}

	@Override
	public List<Lancamento> query(ContribuicaoFilter filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Lancamento.class);
		
        entity_ = query.from(Lancamento.class);
        moradorJoin_ = entity_.join("morador", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Lancamento> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
		
	}

}
