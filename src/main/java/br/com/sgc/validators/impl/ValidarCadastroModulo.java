package br.com.sgc.validators.impl;

import java.util.List;
import java.util.Optional;

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
		
		if(!moduloRepository.findById(id).isPresent())
			errors.getErros().add(new ErroRegistro("", TITULO, " O módulo não existe"));
		
		Optional<Modulo> modulo = moduloRepository.findByDescricao(x.getDescricao());
		if(x.getDescricao().toLowerCase().trim().equals(modulo.get().getDescricao().toLowerCase().trim()) 
				&& !modulo.get().getId().equals(id)) {				
			errors.getErros().add(new ErroRegistro("", TITULO, "Módulo '" + x.getDescricao() + "' já existente para o código " + modulo.get().getId()));
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

	@Override
	public void validarPut(List<AtualizaModuloDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}
}
