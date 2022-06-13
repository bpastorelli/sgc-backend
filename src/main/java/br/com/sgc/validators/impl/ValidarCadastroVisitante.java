package br.com.sgc.validators.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.commons.ValidaCPF;
import br.com.sgc.dto.VisitanteDto;
import br.com.sgc.entities.Visitante;
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
	public void validar(VisitanteDto t) throws RegistroException {
		
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
		
		if(t.getCep().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo CEP é obrigatório"));
		
		if(t.getEndereco().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo Endereço é obrigatório"));
		
		if(t.getNumero().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo Número é obrigatório"));	
		
		if(t.getBairro().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo Bairro é obrigatório"));
		
		if(t.getCidade().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo Cidade é obrigatório"));
		
		if(t.getUf().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " O campo UF é obrigatório"));
		
		if(t.getId() == null) {
			
			this.visitanteRepository.findByRg(t.getRg())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Visitante já cadastrado para o rg "+ t.getRg() +"")));
			
			if(t.getCpf() != null && t.getCpf() != "") {
				this.visitanteRepository.findByCpf(t.getCpf())				
					.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Visitante já cadastrado para o cpf "+ t.getCpf() +"")));
			}
			
			this.visitanteRepository.findByNome(t.getNome())
				.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Visitante já cadastrado para o nome "+ t.getNome() +"")));
			
			t.setPosicao(1L);
			
		}else {
			
			Optional<Visitante> visitanteSource = visitanteRepository.findById(t.getId());
			
			if(!t.getNome().toUpperCase().equals(visitanteSource.get().getNome().toUpperCase())){
				visitanteRepository.findByNome(t.getNome())
					.ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, " Visitante já cadastrado para o nome "+ t.getNome() +" (" + t.getId() + ")")));
			}
			
			if(!t.getCpf().replace(".", "").replace("-", "").equals(visitanteSource.get().getCpf().replace(".", "").replace("-", ""))) {
				if(this.visitanteRepository.findByCpf(t.getCpf()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " O CPF já existe"));
				
				if(!ValidaCPF.isCPF(t.getCpf()))
					errors.getErros().add(new ErroRegistro("", TITULO, " CPF inválido"));
			}
			
		}
				
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

}
