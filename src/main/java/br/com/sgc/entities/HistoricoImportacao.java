package br.com.sgc.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.com.sgc.enums.SituacaoEnum;
import lombok.Data;

@Data
@Entity
@Table(name = "historico_importacao")
public class HistoricoImportacao implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome_arquivo", nullable = false)
	private String nomeArquivo;
	
	@Column(name = "situacao", nullable = false)
	private SituacaoEnum situacao;
	
	@Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;
	
	public HistoricoImportacao() {
		
	}
	
    @PrePersist
    public void prePersist() {
        final LocalDate atual = LocalDate.now();
        dataCriacao = atual;
    }

}