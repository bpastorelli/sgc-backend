package br.com.sgc.repositories.queries.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import br.com.sgc.entities.Visita;
import br.com.sgc.entities.Visitante;
import br.com.sgc.filter.VisitaFilter;
import br.com.sgc.repositories.queries.QueryRepository;

@Repository
public class VisitaQueryRepositoryImpl implements QueryRepository<Visita, VisitaFilter> {

	@PersistenceContext
	private EntityManager manager;
	private static Root<Visita> entity_;
	private static Join<Visita, Visitante> visitanteJoin_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Visita> query;
	
	@Override
	public List<Visita> query(VisitaFilter filters, Pageable pageable) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Visita.class);
		
        entity_ = query.from(Visita.class);
        visitanteJoin_ = entity_.join("visitante", JoinType.INNER);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Visita> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
		
	}

	@Override
	public Predicate[] criarFiltros(Root<Visita> root, VisitaFilter filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getPosicao() != null)
			predicates.add(builder.equal(root.get("posicao"), filters.getPosicao()));
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getNome() != null)
			predicates.add(builder.like(visitanteJoin_.get("nome"), filters.getNome() + '%'));
		
		if(filters.getRg() != null)
			predicates.add(builder.equal(visitanteJoin_.get("rg"), filters.getRg()));
		
		if(filters.getCpf() != null)
			predicates.add(builder.equal(visitanteJoin_.get("cpf"), filters.getCpf()));
		
		if(filters.getDataInicio() != null && filters.getDataFim() != null)
			predicates.add(builder.between(root.get("dataEntrada"), ajustaData(filters.getDataInicio(), true), ajustaData(filters.getDataFim(), false)));
		
		if(filters.getDataInicio() != null && filters.getDataFim() == null)
			predicates.add(builder.between(root.get("dataEntrada"), ajustaData(filters.getDataInicio(), true), ajustaData(filters.getDataInicio(), false)));
		
		if(filters.getGuide() != null)
			predicates.add(builder.equal(root.get("guide"), filters.getGuide()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(VisitaFilter filters) {

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		visitanteJoin_ = entity_.join("visitante", JoinType.INNER);
		entity_.alias("total"); 
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Visita> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}
	
	public Date ajustaData(LocalDate date, boolean startDate) {
		
		Date newDate = new Date();
				
		if(startDate) {
			LocalDateTime date1 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
			newDate = Date.from(date1.atZone(ZoneId.systemDefault()).toInstant());
		}else {
			LocalDateTime date2 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 59, 59);
			newDate = Date.from(date2.atZone(ZoneId.systemDefault()).toInstant());
		}
		
		return newDate;
		
	}

}
