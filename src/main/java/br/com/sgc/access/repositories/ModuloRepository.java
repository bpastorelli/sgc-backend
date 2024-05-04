package br.com.sgc.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.access.entities.Modulo;

@Repository
@Transactional(readOnly = true)
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
	
	Optional<Modulo> findById(Long id);
	
	Optional<Modulo> findByDescricao(String descricao);
		
	Page<Modulo> findAll(Pageable pageable);
	
	Page<Modulo> findByPosicao(Long posicao, Pageable pageable);
	
	List<Modulo> findByPosicao(Long posicao);

}
