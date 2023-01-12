package br.com.sgc.validators.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.AtualizaVinculoResidenciaDto;
import br.com.sgc.dto.VinculoResidenciaDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.repositories.VinculoResidenciaRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarVinculoResidencia implements Validators<VinculoResidenciaDto, AtualizaVinculoResidenciaDto> {
	
	@Autowired
	private VinculoResidenciaRepository vinculoRepository;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private static final String TITULO = "Cadastro de residência recusado!";
	
	@Override
	public void validarPost(VinculoResidenciaDto t) throws RegistroException {
		
		List<VinculoResidenciaDto> residencias = new ArrayList<VinculoResidenciaDto>();
		residencias.add(t);
		
		validar(residencias);
		
	}
	
	@Override
	public void validarPut(AtualizaVinculoResidenciaDto x, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		
	}
	
	public void validar(List<VinculoResidenciaDto> t) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			if(r.getMoradorId() == 0L || r.getMoradorId() == null)
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo morador é obrigatório!")); 
			else {
				if(!this.moradorRepository.findById(r.getMoradorId()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " O morador selecionado não existe!")); 
			}
			
			if(r.getResidenciaId() == 0L || r.getResidenciaId() == null)
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo residencia é obrigatório!")); 
			else {
				if(!this.residenciaRepository.findById(r.getResidenciaId()).isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " A residência selecionada não existe!")); 
			}
			
			if(this.vinculoRepository.findByResidenciaIdAndMoradorId(r.getResidenciaId(), r.getMoradorId()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Este morador já está vinculado a essa residência!")); 
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPost(List<VinculoResidenciaDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
