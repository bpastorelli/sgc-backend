package br.com.sgc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.entities.HistoricoImportacao;
import br.com.sgc.enums.SituacaoEnum;

@Repository
@Transactional(readOnly = true)		
public interface HistoricoImportacaoRepository extends JpaRepository<HistoricoImportacao, Long> {
	
	List<HistoricoImportacao> findBySituacaoIn(List<SituacaoEnum> situacao);

}
