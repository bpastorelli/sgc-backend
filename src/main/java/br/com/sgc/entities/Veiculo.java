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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "veiculo")
public class Veiculo implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666213L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long   id;
	
	@Column(name = "placa", nullable = false)
	private String placa;
	
	@Column(name = "marca", nullable = false)
	private String marca;
	
	@Column(name = "modelo", nullable = false)
	private String modelo;
	
	@Column(name = "cor", nullable = true)
	private String cor;
	
	@Column(name = "ano", nullable = true)
	private Long   ano;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	private Date   dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = false)
	private Date   dataAtualizacao;
	
	@Column(name = "posicao", nullable = false)
	private Long   posicao;

	@Column(name = "guide", nullable = true)
	private String guide;
	
	@PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        this.dataCriacao = atual;
        this.dataAtualizacao = atual;
        this.posicao = 1L;
    }

}
