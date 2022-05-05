package br.com.sgc.commons;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CriteriaSubQuery {
	
	public static long subQueryTotal(EntityManager manager, Root<?> entity_, CriteriaBuilder builder, CriteriaQuery<?> query, Predicate[] predicates) {
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("entitySub"); //use the same alias in order to match the restrictions part and the selection part
		countQuery.select(builder.count(entity_));
		countQuery.where(predicates);

		return manager.createQuery(countQuery).getSingleResult();
		
	}

}
