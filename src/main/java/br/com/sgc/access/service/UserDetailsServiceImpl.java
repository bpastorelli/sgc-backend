package br.com.sgc.access.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.sgc.entities.Morador;
import br.com.sgc.enums.PerfilEnum;
import br.com.sgc.repositories.MoradorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
	private MoradorRepository userRepository;
	
	private List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
	
    public UserDetailsServiceImpl(MoradorRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Morador user = userRepository.findByEmail(username).get();

        if (user == null)
            throw new UsernameNotFoundException(username);
        
        setAuthorities(user.getPerfil());
        
        return new User(user.getEmail(), user.getSenha(), roles);
    }

	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return roles;
	}
	
	public void setAuthorities(PerfilEnum perfil) {
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(perfil.toString());
		this.roles.add(authority);

	}
	
}
