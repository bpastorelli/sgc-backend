package br.com.sgc.security.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.security.constants.SecurityConstants;
import br.com.sgc.security.dto.AlterarSenhaDto;
import br.com.sgc.security.dto.AlterarSenhaResponseDto;
import br.com.sgc.security.dto.JwtAuthenticationDto;
import br.com.sgc.security.dto.TokenDto;
import br.com.sgc.security.utils.JwtTokenUtil;
import br.com.sgc.utils.PasswordUtils;
import br.com.sgc.validators.Validators;

@Service
public class AuthenticationService {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private Validators<JwtAuthenticationDto, AlterarSenhaDto> validar;
	
	private static final String TITULO = "Autenticação"; 

	
	public TokenDto atenticar(JwtAuthenticationDto authenticationDto) throws RegistroException {
		
		String token = null;
		
		this.validar.validarPost(authenticationDto);
    	
		try {
	        Authentication auth = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                   	authenticationDto.getEmail(),
	                      authenticationDto.getSenha(),
	                      new ArrayList<>()));
	          SecurityContextHolder.getContext().setAuthentication(auth);
	              
	          UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
	          token = jwtTokenUtil.obterToken(userDetails, auth);
		}catch(AuthenticationException ex) {
			RegistroException errors = new RegistroException();
			errors.getErros().add(new ErroRegistro("",TITULO, " Senha inválida!"));
			throw errors;
		}
             
     	Optional<Morador> user = this.moradorRepository.findByEmail(authenticationDto.getEmail());
             
        TokenDto dto = TokenDto.builder()
            		.id(user.get().getId())
            		.nome(user.get().getNome())
            		.primeiroAcesso(PasswordUtils.IsPasswordDefault(authenticationDto.getSenha(), user.get()))
            		.token(token)
            		.build();
                    
        return dto;
		
	}
	
	public TokenDto refreshToken(String token) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		//Optional<String> token = Optional.ofNullable(request.getHeader(SecurityConstants.HEADER_STRING));
		
		Optional<String> tokenOpt = Optional.ofNullable(token);
		
		if (tokenOpt.isPresent() && tokenOpt.get().startsWith(SecurityConstants.TOKEN_PREFIX)) {
			tokenOpt = Optional.of(tokenOpt.get().substring(7));
        }
		
		if (!tokenOpt.isPresent()) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token não informado."));
		} else if (!jwtTokenUtil.tokenValido(tokenOpt.get())) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token inválido ou expirado"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
		TokenDto tokenDto = TokenDto.builder()
				.token(tokenOpt.get())
				.build();		
		
		return tokenDto;
		
	}
	
	public AlterarSenhaResponseDto changePassword(AlterarSenhaDto request, Long idUser) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		Optional<Morador> morador = this.moradorRepository.findById(idUser);
		
		TokenDto tokenDto = this.atenticar(JwtAuthenticationDto.builder()
				.email(morador.get().getEmail())
				.senha(request.getSenha())
				.build());
		
		if(tokenDto.getToken() != null) {
		
			morador = PasswordUtils.validarSenha(
							request, 
							idUser, 
							morador.get());
			
		}else {
			errors.getErros().add(new ErroRegistro("", TITULO, " A senha atual está incorreta!"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
		return AlterarSenhaResponseDto.builder()
				.senha(this.moradorRepository.save(morador.get()).getSenha())
				.build();
		
	}
	
	public String reset(Long idUsuario) {
		
		Optional<Morador> morador = this.moradorRepository.findById(idUsuario);
		String senha = PasswordUtils.gerarNovaSenha(10);
		
		morador.get().setSenha(PasswordUtils.gerarBCrypt(senha.toString()));
		this.moradorRepository.save(morador.get());
		
		return senha;
	}
	
}
