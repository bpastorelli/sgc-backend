package br.com.sgc.security.controller;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.response.Response;
import br.com.sgc.security.dto.AlterarSenhaDto;
import br.com.sgc.security.dto.AlterarSenhaResponseDto;
import br.com.sgc.security.dto.JwtAuthenticationDto;
import br.com.sgc.security.dto.TokenDto;
import br.com.sgc.security.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/sgc/token")
@CrossOrigin(origins = "*")
public class AuthenticationController extends RegistroExceptionHandler {
	
	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Gera e retorna um novo token JWT.
	 * 
	 * @param authenticationDto
	 * @param result
	 * @return ResponseEntity<Response<TokenDto>>
	 * @throws AuthenticationException
	 * @throws RegistroException 
	 */
	@PostMapping
	public ResponseEntity<?> gerarTokenJwt(
			@Valid @RequestBody JwtAuthenticationDto authenticationDto)
			throws RegistroException {
		Response<TokenDto> response = new Response<TokenDto>();
		
		log.info("Autenticando...");
		
		response.setData(this.authenticationService.atenticar(authenticationDto));

		return ResponseEntity.ok(response.getData());
	}

	/**
	 * Gera um novo token com uma nova data de expiração.
	 * 
	 * @param request
	 * @return ResponseEntity<Response<TokenDto>>
	 * @throws RegistroException 
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<?> gerarRefreshTokenJwt(
			@RequestParam(value = "token", defaultValue = "null") String token) throws RegistroException {
		
		log.info("Gerando refresh token JWT.");
		
		Response<TokenDto> response = new Response<TokenDto>();
		
		response.setData(this.authenticationService.refreshToken(token));
		
		return ResponseEntity.ok(response.getData());
	}
	
	@PostMapping(value = "/alterarSenha")
	public ResponseEntity<?> alterarSenha(
			@Valid @RequestBody AlterarSenhaDto request,
			@RequestParam(value = "id", defaultValue = "0") Long id) throws RegistroException{
		
		Response<AlterarSenhaResponseDto> response = new Response<AlterarSenhaResponseDto>();
		
		log.info("Alterando senha...");
		
		response.setData(this.authenticationService.changePassword(request, id));
		
		return ResponseEntity.ok(response.getData());
		
	}
	
	@PostMapping(value = "/reset/idUsuario/{idUsuario}")
	public ResponseEntity<?> resetSenha(
			 @PathVariable("idUsuario") Long idUsuario) throws NoSuchAlgorithmException{
		
		log.info("Gerando reset de senha...");
		
		return ResponseEntity.status(200).body(this.authenticationService.reset(idUsuario));
		
	}

}
