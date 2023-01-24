package br.com.sgc.utils;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.security.dto.AlterarSenhaDto;

public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);	
	
	public PasswordUtils() {
	}

	/**
	 * Gera um hash utilizando o BCrypt.
	 * 
	 * @param senha
	 * @return String
	 */
	public static String gerarBCrypt(String senha) {
		if (senha == null) {
			return senha;
		}

		log.info("Gerando hash com o BCrypt.");
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.encode(senha);
	}

	
	/**
	 * Verifica se a senha informada é a padrão
	 * 
	 * @param senha senha atual
	 * @param morador morador atual
	 * @return boolean
	 */
	public static Boolean IsPasswordDefault(String senha, Morador morador) {
		
		String senhaPadrao = morador.getCpf().substring(0, 6);
		
		if(senha.equals(senhaPadrao)) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Validação de senhas
	 * 
	 * @param dto D
	 * @param idUsuario
	 * @param morador
	 * @param result
	 * @return
	 * @throws RegistroException 
	 */
	public static Optional<Morador> validarSenha(AlterarSenhaDto dto, Long idUsuario, Morador morador) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		//Valida se a confirmação de senha está igual a nova senha
		if(!dto.getNovaSenha().equals(dto.getConfirmarSenha()))
			errors.getErros().add(new ErroRegistro("", "", " A confirmação de senha não confere!"));
		
		//Valida se a senha nova senha é igual a senha atual
		if(dto.getNovaSenha().equals(dto.getSenha()))
			errors.getErros().add(new ErroRegistro("", "", " A nova senha não pode ser igual a atual!"));
		
		//Valida se a senha nova senha é igual a senha atual
		if(dto.getNovaSenha().length() < 6)
			errors.getErros().add(new ErroRegistro("", "", " A nova senha deve conter pelo menos 6 caracteres!"));
		
		
		if(!errors.getErros().isEmpty())
			throw errors;

		morador.setSenha(PasswordUtils.gerarBCrypt(dto.getNovaSenha()));
		
		return Optional.ofNullable(morador);
		
	}
	
	public static String gerarNovaSenha(int top) {
		
		Random ran = new Random();
		char data = ' ';
		String dat = "";

		for (int i=0; i<=top; i++) {
		  data = (char)(ran.nextInt(25)+97);
		  dat = data + dat;
		}

		return dat;
		
	}
	
}
