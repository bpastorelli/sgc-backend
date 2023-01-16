package br.com.sgc.validators.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.security.dto.AlterarSenhaDto;
import br.com.sgc.security.dto.JwtAuthenticationDto;
import br.com.sgc.validators.Validators;

@Component
public class ValidarAuthentication implements Validators<JwtAuthenticationDto, AlterarSenhaDto> {

	@Autowired
	private MoradorRepository moradorRepository;
	
	@Override
	public void validarPost(JwtAuthenticationDto dto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
     	Optional<Morador> user = this.moradorRepository.findByEmail(dto.getEmail());
     	
    	if(!user.isPresent())
    		errors.getErros().add(new ErroRegistro("", "Autenticação", " Usuário não encontrado!"));
          
    	if(!errors.getErros().isEmpty())
    		throw errors;
		
	}

	@Override
	public void validarPost(List<JwtAuthenticationDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(AlterarSenhaDto dto, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
