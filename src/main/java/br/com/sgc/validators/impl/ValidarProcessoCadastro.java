package br.com.sgc.validators.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sgc.dto.AtualizaMoradorDto;
import br.com.sgc.dto.AtualizaProcessoCadastroDto;
import br.com.sgc.dto.AtualizaResidenciaDto;
import br.com.sgc.dto.MoradorDto;
import br.com.sgc.dto.ProcessoCadastroDto;
import br.com.sgc.dto.ResidenciaDto;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.ResidenciaRepository;
import br.com.sgc.validators.Validators;

@Component
public class ValidarProcessoCadastro implements Validators<ProcessoCadastroDto, AtualizaProcessoCadastroDto> {

	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private Validators<MoradorDto, AtualizaMoradorDto> validarMorador;
	
	@Autowired
	private Validators<ResidenciaDto, AtualizaResidenciaDto> validarResidencia;
	
	private static final String TITULO = "Processo de cadastro recusado!";
	
	@Override
	public void validarPost(ProcessoCadastroDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		List<MoradorDto> moradores = new ArrayList<MoradorDto>();
		moradores.add(t.getMorador());
		
		if(t.getResidencia().getEndereco().isBlank() || t.getResidencia().getEndereco().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " Campo endereço é obrigatório!")); 
		
		if(t.getResidencia().getNumero() == 0L || t.getResidencia().getNumero() == null)
			errors.getErros().add(new ErroRegistro("", TITULO, " Campo número é obrigatório!")); 
		
		if(t.getResidencia().getCep().isBlank() || t.getResidencia().getCep().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " Campo CEP é obrigatório!"));
		
		if(t.getResidencia().getCidade().isBlank() || t.getResidencia().getCidade().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " Campo Cidade é obrigatório!"));
		
		if(t.getResidencia().getUf().isBlank() || t.getResidencia().getUf().isEmpty())
			errors.getErros().add(new ErroRegistro("", TITULO, " Campo UF é obrigatório!"));
		
		this.validarMorador.validarPost(t.getMorador());
		
		//Se a residencia não existir, valida...
		if(!this.residenciaRepository.findByCepAndNumeroAndComplemento(t.getResidencia().getCep(), t.getResidencia().getNumero(), t.getResidencia().getComplemento()).isPresent())
			this.validarResidencia.validarPost(t.getResidencia());
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(AtualizaProcessoCadastroDto x, Long id) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPost(List<ProcessoCadastroDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(List<AtualizaProcessoCadastroDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
