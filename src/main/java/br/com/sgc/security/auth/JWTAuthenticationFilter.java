package br.com.sgc.security.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import br.com.sgc.entities.Morador;
import br.com.sgc.repositories.MoradorRepository;
import br.com.sgc.security.constants.SecurityConstants;
import br.com.sgc.security.dto.TokenDto;
import br.com.sgc.utils.PasswordUtils;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	private Morador creds;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, MoradorRepository moradorRepository) {
        this.authenticationManager = authenticationManager;
        this.moradorRepository = moradorRepository;

        setFilterProcessesUrl("/sgc/login"); 
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            this.creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Morador.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getSenha(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        
        Morador morador = this.moradorRepository.findByEmail(this.creds.getEmail()).get();
        
        TokenDto dto = TokenDto.builder()
        		.id(morador.getId())
        		.nome(morador.getNome())
        		.primeiroAcesso(PasswordUtils.IsPasswordDefault(this.creds.getSenha() ,morador))
        		.token(token)
        		.build();
        
        String tokenDtoJsonString = new Gson().toJson(dto);
        
        res.getWriter().write(tokenDtoJsonString);
        res.getWriter().flush();
        
    }
    

}
