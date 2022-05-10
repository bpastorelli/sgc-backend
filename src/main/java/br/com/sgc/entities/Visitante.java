package br.com.sgc.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visitante")
public class Visitante implements Serializable {
	
	private static final long serialVersionUID = -5754246207015712519L;
	
	@Column(name = "guide", nullable = true)
	private String guide;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long   id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "cpf", nullable = true)
	private String cpf;
	
	@Column(name = "rg", nullable = false)
	private String rg;
	
	@Column(name = "endereco", nullable = true)
	private String endereco;
	
	@Column(name = "numero", nullable = true)
	private String numero;
	
	@Column(name = "cep", nullable = true)
	private String cep;
	
	@Column(name = "complemento", nullable = true)
	private String complemento;
	
	@Column(name = "bairro", nullable = true)
	private String bairro;
	
	@Column(name = "cidade", nullable = true)
	private String cidade;
	
	@Column(name = "uf", nullable = true)
	private String uf;
	
	@Column(name="telefone", nullable = true)
	private String telefone;
	
	@Column(name = "celular", nullable = false)
	private String celular;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	private Date   dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = false)
	private Date   dataAtualizacao;
	
	@Transient
	private String ultimaVisita;
	
	@Column(name = "posicao", nullable = false)
	private Long   posicao;
	
	@PreUpdate
	public void preUpdate() {
		
		dataAtualizacao = new Date();
		
	}
	
	@PrePersist
	public void prePersist() {
		
        final Date atual = new Date();
        final long status = 1;
        dataCriacao = atual;
        dataAtualizacao = atual;
        posicao = status;
        
	}
	
	
	@Override
	public String toString() {
		return "Morador [id=" + id + ", nome=" + nome + ", cpf=" + cpf
				+ ", rg=" + rg + ", telefone=" + telefone + ", celular=" + celular + ", cep=" + cep + ", endereco=" + endereco + ""
				+ ", numero=" + numero + ", bairro=" + bairro + ", complemento=" + complemento + ", cidade=" + cidade + ", uf=" + uf + ", dataCriacao="
				+ dataCriacao + ", dataAtualizacao=" + dataAtualizacao + ", posicao=" + posicao + ", guide=" + guide + "]";
	}
	
}
