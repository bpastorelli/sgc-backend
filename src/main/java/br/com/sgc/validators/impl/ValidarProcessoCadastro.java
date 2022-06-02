package br.com.sgc.validators.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.validators.Validators;

@Component
public class ValidarProcessoCadastro implements Validators<ProcessoCadastroDto> {

	@Autowired
	private Validators<List<MoradorDto>> validarMorador;
	
	@Autowired
	private Validators<ResidenciaDto> validarResidencia;
	
	@Override
	public void validar(ProcessoCadastroDto t) throws RegistroException {
		
		List<MoradorDto> moradores = new ArrayList<MoradorDto>();
		moradores.add(t.getMorador());
		
		this.validarMorador.validar(moradores);
		
		this.validarResidencia.validar(t.getResidencia());
		
	}

}
