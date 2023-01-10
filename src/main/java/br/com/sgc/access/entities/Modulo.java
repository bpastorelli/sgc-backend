package br.com.sgc.access.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "modulo")
public class Modulo implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "path_modulo", nullable = true)
	private String pathModulo;
	
	@Column(name = "posicao", nullable = false)
	private Long posicao;
	
    @PrePersist
    public void prePersist() {
        final long status = 1;
        posicao = status;
    }

}
