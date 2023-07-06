package br.com.sgc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.entities.HistoricoImportacao;

@Repository
@Transactional(readOnly = true)		
public interface HistoricoImportacaoRepository extends JpaRepository<HistoricoImportacao, Long> {

}
