package br.com.sgc.access.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.sgc.entities.Morador;

public class MyUserPrincipal implements UserDetails  {


	private static final long serialVersionUID = 1L;
	
	private Morador morador;
	
    public MyUserPrincipal(Morador morador) {
        this.morador = morador;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return morador.getSenha();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return morador.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
	
		return this.morador.getPosicao() == 1 ? true : false;
	}

}
