package br.com.sgc.validators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.commons.ValidaCPF;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroMorador implements Validators<List<MoradorDto>> {
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private static final String TITULO = "Cadastro de morador recusado!";
	
	@Override
	public List<ErroRegistro> validar(List<MoradorDto> t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(t.size() == 0) 
			errors.getErros().add(new ErroRegistro("", TITULO, " Você deve informar ao menos um morador"));	
		
		for(MoradorDto morador : t) {
			
			morador.setResidenciaId(morador.getResidenciaId() == null ? 0 : morador.getResidenciaId());
			
			if(morador.getNome().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Nome é obrigatório"));
			
			if(morador.getCpf().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo CPF é obrigatório"));
			
			if(!ValidaCPF.isCPF(morador.getCpf()))
				errors.getErros().add(new ErroRegistro("", TITULO, " CPF " + morador.getCpf() + " inválido"));
			
			if(morador.getRg().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo RG é obrigatório"));
			
			if(morador.getEmail().isEmpty())
				errors.getErros().add(new ErroRegistro("" , TITULO, " O campo e-mail é obrigatório"));
			
			if(morador.getTelefone().isEmpty() && morador.getCelular().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Você deve informar um número de telefone ou celular"));
			
		}

		t.forEach(morador ->{
			this.moradorRepository.findByNome(morador.getNome())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Nome '" + morador.getNome() + "' já existe")));	
		});
		
		t.forEach(morador ->{
			this.moradorRepository.findByCpf(morador.getCpf())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " CPF '" + morador.getCpf() + "' já existe")));	
		});
		
		t.forEach(morador ->{
			this.moradorRepository.findByRg(morador.getRg())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " RG '" + morador.getRg() + "' já existe")));	
		});
	
		t.forEach(morador ->{
			this.moradorRepository.findByEmail(morador.getEmail())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " E-mail '" + morador.getEmail() + "' já existe")));	
		});
		
		t.forEach(morador -> {
			if(!morador.getResidenciaId().equals(0L)) {
				if (!this.residenciaRepository.findById(morador.getResidenciaId()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " A residencia de código '" + morador.getResidenciaId() + "' não existe"));				
			}
		});
		
		//Valida se o CPF não está duplicado na requisição.
		t.forEach(morador -> {
			if(t
					.stream()
					.filter(pessoa -> pessoa.getCpf()
					.equals(morador.getCpf())).count() > 1)
				errors.getErros().add(new ErroRegistro("", TITULO, " CPF '" + morador.getCpf() + "' está duplicado"));
		});	
		
		//Valida se o RG não está duplicado na requisição.
		t.forEach(morador -> {
			if(t
					.stream()
					.filter(pessoa -> pessoa.getRg()
					.equals(morador.getRg())).count() > 1)
				errors.getErros().add(new ErroRegistro("", TITULO, " RG '" + morador.getRg() + "' está duplicado"));
		});
		
		//Valida se o E-mail não está duplicado na requisição.
		t.forEach(morador -> {
			if(t
				.stream()
				.filter(pessoa -> pessoa.getEmail()
				.equals(morador.getEmail())).count() > 1) {
				errors.getErros().add(new ErroRegistro("", TITULO, " E-mail '" + morador.getEmail() + "' está duplicado"));				
			}
		});

		return errors.getErros();
		
	}

}
