package br.com.sgc.security.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.errorheadling.RegistroExceptionHandler;
import br.com.sgc.response.Response;
import br.com.sgc.security.dto.AlterarSenhaDto;
import br.com.sgc.security.dto.AlterarSenhaResponseDto;
import br.com.sgc.security.dto.JwtAuthenticationDto;
import br.com.sgc.security.dto.TokenDto;
import br.com.sgc.security.service.AuthenticationService;
import br.com.sgc.security.utils.JwtTokenUtil;



@RestController
@RequestMapping("/sgc/token")
@CrossOrigin(origins = "*")
public class AuthenticationController extends RegistroExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	private static final String TOKEN_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String TITULO = "Autenticação"; 

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
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
			
		response.setData(this.authenticationService.atenticar(authenticationDto));

		return ResponseEntity.ok(response.getData());
	}

	/**
	 * Gera um novo token com uma nova data de expiração.
	 * 
	 * @param request
	 * @return ResponseEntity<Response<TokenDto>>
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<?> gerarRefreshTokenJwt(HttpServletRequest request) {
		log.info("Gerando refresh token JWT.");
		
		RegistroException errors = new RegistroException();
		
		Response<TokenDto> response = new Response<TokenDto>();
		Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));
		
		if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
			token = Optional.of(token.get().substring(7));
        }
		
		if (!token.isPresent()) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token não informado."));
		} else if (!jwtTokenUtil.tokenValido(token.get())) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token inválido ou expirado"));
		}
		
		if (!errors.getErros().isEmpty()) { 
			return ResponseEntity.badRequest().body(response);
		}
		
		//String refreshedToken = jwtTokenUtil.refreshToken(token.get());
		//response.setData(new TokenDto(refreshedToken));
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/alterarSenha")
	public ResponseEntity<?> alterarSenha(
			@Valid @RequestBody AlterarSenhaDto request,
			@RequestParam(value = "id", defaultValue = "0") Long id) throws RegistroException{
		
		Response<AlterarSenhaResponseDto> response = new Response<AlterarSenhaResponseDto>();
		
		response.setData(this.authenticationService.changePassword(request, id));
		
		return ResponseEntity.ok(response.getData());
		
	}
	
	@PostMapping(value = "/reset/idUsuario/{idUsuario}")
	public ResponseEntity<?> resetSenha(
			 @PathVariable("idUsuario") Long idUsuario) throws NoSuchAlgorithmException{
		
		return ResponseEntity.status(200).body(this.authenticationService.reset(idUsuario));
		
	}

}
