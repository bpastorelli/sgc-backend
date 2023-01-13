package br.com.sgc.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.sgc.entities.Morador;
import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.security.dto.AlterarSenhaDto;
import br.com.sgc.security.dto.AlterarSenhaResponseDto;
import br.com.sgc.security.dto.JwtAuthenticationDto;
import br.com.sgc.security.dto.TokenDto;
import br.com.sgc.security.utils.JwtTokenUtil;
import br.com.sgc.utils.PasswordUtils;

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
	
	public TokenDto atenticar(JwtAuthenticationDto authenticationDto) throws RegistroException {
		
		RegistroException errors = new RegistroException();

     	Optional<Morador> user = this.moradorRepository.findByEmail(authenticationDto.getEmail());
    	if(!user.isPresent())
    		errors.getErros().add(new ErroRegistro("", "Autenticação", " Usuário inexistente!"));
          
    	if(!errors.getErros().isEmpty())
    		throw errors;
    		
        Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                 	authenticationDto.getEmail(),
                    authenticationDto.getSenha(),
                    new ArrayList<>()));
        SecurityContextHolder.getContext().setAuthentication(auth);
            
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
        String token = jwtTokenUtil.obterToken(userDetails, auth);
             
     	Optional<Morador> usuario = this.moradorRepository.findByEmail(authenticationDto.getEmail());
    	if(!usuario.isPresent())
    		errors.getErros().add(new ErroRegistro("", "Autenticação", " Usuário inexistente!"));
    		
    	if(!errors.getErros().isEmpty())
    		throw errors;
             
        TokenDto dto = TokenDto.builder()
            		.id(user.get().getId())
            		.nome(user.get().getNome())
            		.primeiroAcesso(PasswordUtils.IsPasswordDefault(authenticationDto.getSenha(), user.get()))
            		.token(token)
            		.build();
                    
        return dto;
		
	}
	
	public AlterarSenhaResponseDto changePassword(AlterarSenhaDto request, Long idUser) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		List<Morador> moradorList = new ArrayList<Morador>();
		
		Morador morador = this.moradorRepository.findById(idUser).get();
		
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(morador.getEmail(), request.getSenha()));
		SecurityContextHolder.getContext().setAuthentication(auth);

		UserDetails userDetails = userDetailsService.loadUserByUsername(morador.getEmail());
		String token = jwtTokenUtil.obterToken(userDetails, auth);
		
		if(token != null) {
		
			morador = PasswordUtils.validarSenha(
							request, 
							idUser, 
							morador).get();

			moradorList.add(morador);
			
		}else {
			errors.getErros().add(new ErroRegistro("", "Autenticação", " A senha atual está incorreta!"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
		return AlterarSenhaResponseDto.builder()
				.senha(this.moradorRepository.save(morador).getSenha())
				.build();
		
	}
	
}
