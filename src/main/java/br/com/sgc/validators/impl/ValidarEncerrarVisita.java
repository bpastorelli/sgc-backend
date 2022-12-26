package br.com.sgc.validators.impl;

import java.sql.Time;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.EncerraVisitaDto;
import br.com.sgc.entities.Visita;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.VisitaRepository;
import br.com.sgc.utils.Utils;
import br.com.sgc.validators.Validators;

@Component
public class ValidarEncerrarVisita implements Validators<EncerraVisitaDto> {

	@Value("${guide.size}")
	private int guideSize;
	
	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	private static final String TITULO = "Encerramento de visita recusado!";
	
	@Override
	public void validar(EncerraVisitaDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(t.getId() != null || t.getId() != 0) {
			Optional<Visita> visita = visitaRepository.findById(t.getId());
			
			if(!visita.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Visita não encontrada!"));
			
			if(visita.get().getPosicao() == 0) 
				errors.getErros().add(new ErroRegistro("", TITULO, " Esta visita já foi encerrada em " + Utils.dateFormat(visita.get().getDataSaida(), "dd/MM/yyyy") + " às " + new Time(visita.get().getDataSaida().getTime()) + "!"));	
		
		}
		
	}

}
