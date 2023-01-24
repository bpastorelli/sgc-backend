package br.com.sgc.validators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.access.dto.AtualizaAcessoFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroAcessoFuncionalidadeDto;
import br.com.sgc.access.repositories.FuncionalidadeRepository;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarAcessoFuncionalidade implements Validators<CadastroAcessoFuncionalidadeDto, AtualizaAcessoFuncionalidadeDto> {

	@Autowired
	private MoradorRepository moradorRepository;

	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private FuncionalidadeRepository funcionalidadeRepository;
	
	private static final String TITULO = "Cadastro de acesso funcionalidade recusado!";
	
	@Override
	public void validarPost(CadastroAcessoFuncionalidadeDto dto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPost(List<CadastroAcessoFuncionalidadeDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(AtualizaAcessoFuncionalidadeDto dto, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(List<AtualizaAcessoFuncionalidadeDto> listDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		listDto.forEach(d -> {
		
			if(!this.moradorRepository.findById(d.getIdUsuario()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.moduloRepository.findById(d.getIdModulo()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
			if(!this.funcionalidadeRepository.findById(d.getIdFuncionalidade()).isPresent()) {
				errors.getErros().add(new ErroRegistro("", TITULO, "Funcionalidade inexistente para o código " + d.getIdFuncionalidade()));
			}
		
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

}
