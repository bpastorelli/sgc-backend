package br.com.sgc.validators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.access.dto.AtualizaAcessoModuloDto;
import br.com.sgc.access.dto.CadastroAcessoModuloDto;
import br.com.sgc.access.repositories.AcessoModuloRepository;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarAcessoModulo implements Validators<CadastroAcessoModuloDto, AtualizaAcessoModuloDto> {

	@Autowired
	private MoradorRepository moradorRepository;	
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private AcessoModuloRepository acessoModuloRepository;
	
	private static final String TITULO = "Cadastro de acesso módulo recusado!";
	
	@Override
	public void validarPost(CadastroAcessoModuloDto dto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPost(List<CadastroAcessoModuloDto> listDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		listDto.forEach(d -> {
			if(!this.moradorRepository.findById(d.getIdUsuario()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.moduloRepository.findById(d.getIdModulo()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Módulo inexistente para o código " + d.getIdUsuario()));
			}
			
			if(this.acessoModuloRepository.findByIdUsuarioAndModuloId(d.getIdUsuario(), d.getIdModulo()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Acesso já concedido para este módulo " + d.getIdModulo() + " e usuário"));
			}
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(AtualizaAcessoModuloDto dto, Long id) throws RegistroException {
		

	}

	@Override
	public void validarPut(List<AtualizaAcessoModuloDto> listDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		listDto.forEach(d -> {
			
			if(!this.moradorRepository.findById(d.getIdUsuario()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.moduloRepository.findById(d.getIdModulo()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Módulo inexistente para o código " + d.getIdUsuario()));
			}
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

}
