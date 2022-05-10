package br.com.sgc.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgc.entities.Veiculo;

@Repository
@Transactional(readOnly = true)
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
	Optional<Veiculo> findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Veiculo> findByIdOrPlacaOrMarcaOrModelo(Long id, String placa, String marca, String modelo, Pageable pageable);
	
	Optional<Veiculo> findByPlaca(String placa);
	
	Optional<Veiculo> findByGuide(String guide);

}
