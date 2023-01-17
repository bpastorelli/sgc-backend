package br.com.sgc.access.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "acesso_modulo")
public class AcessoModulo implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_usuario", nullable = false)
	private Long idUsuario;
	
	@ManyToOne
	@JoinColumn(name="id_modulo")
	private Modulo modulo;
	
	@Column(name = "acesso", nullable = false)
	private boolean acesso;
	
	@Column(name = "data_cadastro", nullable = false)
	private Date dataCadastro;
	
	@Column(name = "posicao", nullable = false)
	private Long posicao;
	
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        final long status = 1;
        dataCadastro = atual;
        posicao = status;
    }
}
