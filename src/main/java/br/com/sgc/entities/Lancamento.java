package br.com.sgc.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "morador_id", nullable = false)
	private Long moradorId;
	
	@Column(name = "data_pagamento", nullable = false)
	private Date dataPagamento;
	
	@Column(name = "mes_referencia", nullable = false)
	private String periodo;
	
	@Column(name = "documento", nullable = false)
	private String documento;
	
	@Column(name = "valor", nullable = false)
	private BigDecimal valor;
	
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;
	
	@Column(name = "residencia_id", nullable = false)
	private Long residenciaId;
	
	public Lancamento() {
		
	}
	
	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }
	
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

}