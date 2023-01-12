package br.com.sgc.validators.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.entities.Residencia;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroResidencia implements Validators<ResidenciaDto, AtualizaResidenciaDto> {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	private static final String TITULO = "Cadastro de residência recusado!";
	
	@Override
	public void validarPost(ResidenciaDto t) throws RegistroException {
		
		List<ResidenciaDto> residencias = new ArrayList<ResidenciaDto>();
		residencias.add(t);
		
		validarPost(residencias);
		
	}
	
	@Override
	public void validarPut(AtualizaResidenciaDto t, Long id) throws RegistroException {
		
		List<AtualizaResidenciaDto> residencias = new ArrayList<AtualizaResidenciaDto>();
		residencias.add(t);
		
		validarPut(residencias, id);
		
	}
	
	@Override
	public void validarPost(List<ResidenciaDto> t) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			if(r.getEndereco().isBlank() || r.getEndereco().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo endereço é obrigatório!")); 
				
			if(r.getNumero() == 0L || r.getNumero() == null)
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo número é obrigatório!")); 
				
			if(r.getCep().isBlank() || r.getCep().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo CEP é obrigatório!"));
				
			if(r.getCidade().isBlank() || r.getCidade().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo Cidade é obrigatório!"));
				
			if(r.getUf().isBlank() || r.getUf().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo UF é obrigatório!"));
				
			this.residenciaRepository.findByCepAndNumero(r.getCep(), r.getNumero())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Endereço já existente")));

			if(r.getTicketMorador() != null) {			
				if(!this.moradorRepository.findByGuide(r.getTicketMorador()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " Morador a ser vinculado não encontrado"));
			}	
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}
	
	public void validarPut(List<AtualizaResidenciaDto> t, Long id) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			Optional<Residencia> residenciaSource = this.residenciaRepository.findById(r.getId());
				
			if(residenciaSource.isPresent()) {
				if(!r.getCep().equals(residenciaSource.get().getCep()) && !r.getNumero().equals(residenciaSource.get().getNumero())) {
					if(this.residenciaRepository.findByCepAndNumero(r.getCep(), r.getNumero()).isPresent())
						errors.getErros().add(new ErroRegistro("", TITULO, " Não é possível realizar uma alteração de endereço para um endereço já existente!"));
				}	
			}else {
				errors.getErros().add(new ErroRegistro("", TITULO, " Residência não encontrada!"));
			}
									
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

}
