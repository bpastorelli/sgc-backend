package br.com.sgc.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.access.entities.Funcionalidade;

@Repository
@Transactional(readOnly = true)
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {
	
	Optional<Funcionalidade> findById(Long id);
	
	Optional<Funcionalidade> findByDescricao(String descricao);
		
	Page<Funcionalidade> findAll(Pageable pageable);
	
	Page<Funcionalidade> findByPosicao(Long posicao, Pageable pageable);
	
	List<Funcionalidade> findByPosicao(Long posicao);

}
