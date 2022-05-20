package br.com.sgc.validators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.commons.ValidaCPF;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.VisitanteRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroVisitante implements Validators<VisitanteDto> {

	@Autowired
	private VisitanteRepository visitanteRepository;
	
	private static final String TITULO = "Cadastro de visitante recusado!";
	
	@Override
	public List<ErroRegistro> validar(VisitanteDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(t.getNome().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo nome é obrigatório"));
		
		//Valida se o campo RG foi preenchido
		if(t.getRg().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo RG é obrigatório"));	
		
		//Se o campo CPF for informado, valida se é um CPF válido
		if(!t.getCpf().isEmpty()) {
			if(!ValidaCPF.isCPF(t.getCpf()))
				errors.getErros().add(new ErroRegistro("", TITULO, " CPF inválido"));
		}		
		
		this.visitanteRepository.findByRg(t.getRg())
			.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Vistante já cadastrado para o rg "+ t.getRg() +"")));
		
		this.visitanteRepository.findByRg(t.getRg())
			.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Vistante já cadastrado para o cpf "+ t.getCpf() +"")));
		
		this.visitanteRepository.findByNome(t.getNome())
			.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Vistante já cadastrado para o nome "+ t.getNome() +"")));
		
		return errors.getErros();
		
	}

}
