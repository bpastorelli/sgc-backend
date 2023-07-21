package br.com.sgc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.entities.Morador;

@Repository
@Transactional(readOnly = true)
public interface MoradorRepository extends JpaRepository<Morador, Long> {
	
	Optional<Morador> findByNome(String nome);
	
	Optional<Morador> findByNomeAndCpf(String nome, String cpf);
	
	Optional<Morador> findByCpf(String cpf);
	
	List<Morador> findByCpfIn(List<String> cpfs);
	
	Optional<Morador> findByRg(String rg);
	
	Optional<Morador> findByEmail(String email);
	
	Optional<Morador> findByEmailAndPosicao(String email, Long posicao);
	
	Optional<Morador> findByGuide(String guide);
	
	Optional<Morador> findByEmailAndSenha(String username, String senha);
	
	@Transactional(readOnly = true)
	Page<Morador> findByIdOrCpfOrRgOrNomeContainsOrEmailOrPosicao(Long id, String cpf, String rg, String nome, String email, Long posicao, Pageable pageable);
	
	Page<Morador> findByPosicao(Long posicao, Pageable pageable);
	
	
	
}
