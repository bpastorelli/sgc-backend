package br.com.sgc.validators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.access.dto.AtualizaModuloDto;
import br.com.sgc.access.dto.CadastroModuloDto;
import br.com.sgc.access.entities.Modulo;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroModulo implements Validators<CadastroModuloDto, AtualizaModuloDto> {

	@Autowired
	private ModuloRepository moduloRepository;
	
	private static final String TITULO = "Cadastro de módulo recusado!";
	
	@Override
	public void validarPost(CadastroModuloDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
			
		if(this.moduloRepository.findByDescricao(t.getDescricao()).isPresent()) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Módulo '" + t.getDescricao() + "' já existe"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(AtualizaModuloDto x, Long id) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		Modulo modulo = moduloRepository.findByDescricao(x.getDescricao()).get();
		
		if(x.getDescricao().toLowerCase().trim().equals(modulo.getDescricao().toLowerCase().trim()) 
				&& !modulo.getId().equals(id)) {				
			errors.getErros().add(new ErroRegistro("", TITULO, "Módulo '" + x.getDescricao() + "' já existente para o código " + modulo.getId()));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
	}

	@Override
	public void validarPost(List<CadastroModuloDto> listDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		listDto.forEach(t -> {
			if(this.moduloRepository.findByDescricao(t.getDescricao()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Módulo '" + t.getDescricao() + "' já existe"));
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}
}
