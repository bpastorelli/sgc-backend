package br.com.sgc.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.access.entities.AcessoModulo;

@Repository
@Transactional(readOnly = true)
public interface AcessoModuloRepository extends JpaRepository<AcessoModulo, Long> {
	
	List<AcessoModulo> findByIdUsuarioAndAcesso(Long idUsuario, boolean acesso);
	
	Optional<AcessoModulo> findById(Long id);
	
	Optional<AcessoModulo> findByIdUsuarioAndModuloId(Long idUsuario, Long idModulo); 
	
	List<AcessoModulo> findByIdUsuario(Long idUsuario);

}
