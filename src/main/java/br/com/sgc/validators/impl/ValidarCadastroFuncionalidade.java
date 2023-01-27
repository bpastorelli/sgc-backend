package br.com.sgc.validators.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.access.dto.AtualizaFuncionalidadeDto;
import br.com.sgc.access.dto.CadastroFuncionalidadeDto;
import br.com.sgc.access.entities.Funcionalidade;
import br.com.sgc.access.repositories.FuncionalidadeRepository;
import br.com.sgc.access.repositories.ModuloRepository;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroFuncionalidade implements Validators<CadastroFuncionalidadeDto, AtualizaFuncionalidadeDto> {

	
	@Autowired
	private FuncionalidadeRepository funcionalidadeRepository;	
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	private static final String TITULO = "Cadastro de funcionalidade recusado!";
	
	@Override
	public void validarPost(CadastroFuncionalidadeDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
			
		if(!moduloRepository.findById(t.getIdModulo()).isPresent())
			errors.getErros().add(new ErroRegistro("", TITULO, " O módulo não existe"));
		
		if(this.funcionalidadeRepository.findByDescricaoAndIdModulo(t.getDescricao(), t.getIdModulo()).isPresent()) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Funcionalidade '" + t.getDescricao() + "' já existe para o módulo"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(AtualizaFuncionalidadeDto x, Long id) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(!funcionalidadeRepository.findById(id).isPresent())
			errors.getErros().add(new ErroRegistro("", TITULO, " O funcionalidade não existe"));
		
		Optional<Funcionalidade> funcionalidade = funcionalidadeRepository.findByDescricao(x.getDescricao());
		if(x.getDescricao().toLowerCase().trim().equals(funcionalidade.get().getDescricao().toLowerCase().trim()) 
				&& !funcionalidade.get().getId().equals(id)) {				
			errors.getErros().add(new ErroRegistro("", TITULO, "Funcionalidade '" + x.getDescricao() + "' já existente para o código " + funcionalidade.get().getId()));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
	}

	@Override
	public void validarPost(List<CadastroFuncionalidadeDto> listDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		listDto.forEach(t -> {
			if(!moduloRepository.findById(t.getIdModulo()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " O módulo não existe"));
			
			if(this.funcionalidadeRepository.findByDescricaoAndIdModulo(t.getDescricao(), t.getIdModulo()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Funcionalidade '" + t.getDescricao() + "' já existe para esse módulo"));
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(List<AtualizaFuncionalidadeDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}
}
