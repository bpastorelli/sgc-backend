package br.com.sgc.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sgc.entities.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	Optional<Lancamento> findByPeriodoAndId(String periodo, Long id);
	
	List<Lancamento> findByMoradorIdIn(List<Long> codigos);
	
	@Query(value = "select * "
			+ " from lancamento l "
			+ " inner join morador m "
			+ " where (m.cpf = :#{#cpf}) "
			+ " and (l.valor = :#{#valor}) "
			+ " and DATE(l.data_pagamento) = :#{#dataPagamento}", nativeQuery = true)
	List<Lancamento> findByCpfAndValorAndDataPagamento(String cpf, BigDecimal valor, LocalDate dataPagamento);
	
}
