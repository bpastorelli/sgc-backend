package br.com.sgc.validators.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.VeiculoDto;
import br.com.sgc.entities.Visitante;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.VeiculoRepository;
import br.com.sgc.repositories.VisitanteRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarCadastroVeiculo implements Validators<VeiculoDto> {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	private static final String TITULO = "Cadastro de veículo recusado!";
	
	@Override
	public void validar(VeiculoDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(t.getTicketVisitante() == null && t.getVisitanteId() == null)
			errors.getErros().add(new ErroRegistro("", TITULO, " Visitante responsável não informado!"));
		
		if(t.getTicketVisitante() == "" && t.getVisitanteId() == null)
			errors.getErros().add(new ErroRegistro("", TITULO, " Visitante responsável não informado!"));
		
		if(t.getTicketVisitante() != "" && t.getTicketVisitante() != null) {
			Optional<Visitante> visitante = this.visitanteRepository.findByGuide(t.getTicketVisitante());
			if(!visitante.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " O visitante informado não existe!"));
			else {
				t.setVisitanteId(visitante.get().getId());
			}
		}
		
		if(t.getVisitanteId() != null) {
			if(!this.visitanteRepository.findById(t.getVisitanteId()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " O visitante informado (" + t.getVisitanteId() + ") não existe!"));			
		}
		
		if(t.getPlaca() == null) 
			errors.getErros().add(new ErroRegistro("", TITULO, " Não existem veículos para cadastro!"));
		
		this.veiculoRepository.findByPlaca(t.getPlaca().replace("-", "")).
			ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, "A placa informada (" + t.getPlaca() + ") já existe para o veiculo id " + res.getId() + "!") ));
		
//		if(!this.serviceVeiculo.buscarPorId(t.getVisitanteId()).isPresent())
//			result.addError(new ObjectError("veiculo", "Visitante inexistente!"));		
		
//		this.serviceVinculoVeiculo.buscarPorPlacaAndVisitanteId(t.getPlaca().replace("-", ""), t.getVisitanteId()).
//			ifPresent(res -> result.addError(new ObjectError("veiculo", "Veiculo de placa " + t.getPlaca() + " já vinculado para esta pessoa!")));
		t.setPlaca(t.getPlaca().replace("-", ""));
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

}
