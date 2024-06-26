package br.com.sgc.access.entities;

import java.io.Serializable;
import java.util.Date;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "acesso_funcionalidade")
public class AcessoFuncionalidade implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_usuario", nullable = false)
	private Long idUsuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_modulo")
	private Modulo modulo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_funcionalidade")
	private Funcionalidade funcionalidade;
	
	@Column(name = "acesso", nullable = false)
	private boolean acesso;
	
	@Column(name = "inclusao", nullable = false)
	private boolean inclusao;
	
	@Column(name = "alteracao", nullable = false)
	private boolean alteracao;
	
	@Column(name = "exclusao", nullable = false)
	private boolean exclusao;
	
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
