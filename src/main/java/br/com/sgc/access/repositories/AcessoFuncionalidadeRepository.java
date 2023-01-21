package br.com.sgc.access.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.access.entities.AcessoFuncionalidade;

@Repository
@Transactional(readOnly = true)
public interface AcessoFuncionalidadeRepository extends JpaRepository<AcessoFuncionalidade, Long> {
	
	Optional<AcessoFuncionalidade> findByIdUsuarioAndFuncionalidadeIdAndModuloId(Long idUsuario, Long idFuncionalidade, Long idModulo);
	
}
