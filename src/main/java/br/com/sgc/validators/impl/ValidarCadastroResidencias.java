package br.com.sgc.validators.impl;

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
public class ValidarCadastroResidencias implements Validators<ResidenciaDto, List<AtualizaResidenciaDto>> {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	private static final String TITULO = "Cadastro de residência recusado!";
	
	@Override	
	public void validarPost(List<ResidenciaDto> t) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			this.residenciaRepository.findByCepAndNumeroAndComplemento(r.getCep(), r.getNumero(), r.getComplemento().toUpperCase())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Endereço já existente")));

			if(r.getTicketMorador() != null) {			
				if(!this.moradorRepository.findByGuide(r.getTicketMorador()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " Morador a ser vinculado não encontrado"));
			}
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(List<AtualizaResidenciaDto> t, Long id) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			Optional<Residencia> residencia = this.residenciaRepository.findByCepAndNumeroAndComplemento(r.getCep(), r.getNumero(), r.getComplemento());
			
			if(!residencia.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Endereço não encontrado!"));
			
			if(residencia.isPresent()) {
				if(!r.getCep().equals(residencia.get().getCep()) && !r.getNumero().equals(residencia.get().getNumero())) {
					if(this.residenciaRepository.findByCepAndNumeroAndComplemento(r.getCep(), r.getNumero(), r.getComplemento().toUpperCase()).isPresent())
						errors.getErros().add(new ErroRegistro("", TITULO, " Não é possível realizar uma alteração de endereço para um endereço já existente!"));
				}	
			}else {
				errors.getErros().add(new ErroRegistro("", TITULO, " Residência não encontrada!"));
			}
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPost(ResidenciaDto dto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(List<List<AtualizaResidenciaDto>> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
