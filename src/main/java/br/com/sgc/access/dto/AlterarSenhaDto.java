package br.com.sgc.access.dto;

import javax.validation.constraints.NotNull;

public class AlterarSenhaDto {

	private String senha;
	private String novaSenha;
	private String confirmarSenha;
	
	public AlterarSenhaDto() {
		
	}
	
	@NotNull(message = "O campo senha é obrigatório!")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@NotNull(message = "O campo nova senha é obrigatório!")
	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	@NotNull(message = "O campo confirmação de senha é obrigatório!")
	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
}
