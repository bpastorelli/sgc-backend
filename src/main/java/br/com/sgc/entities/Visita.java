package br.com.sgc.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long       id;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Visitante  visitante;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Residencia residencia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrada", nullable = false)
	private Date       dataEntrada;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "hora_entrada", nullable = false)
	private Date       horaEntrada;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_saida", nullable = false)
	private Date       dataSaida;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_saida", nullable = false)
	private Date       horaSaida;
	
	@Column(name = "placa", nullable = true)
	private String     placa;
	
	@Column(name = "posicao", nullable = false)
	private Integer    posicao;
	
	@Column(name = "guide", nullable = true)
	private String     guide;
	
	@PrePersist
	public void prePersist() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 1;
        
        dataEntrada = dataAtual;
        horaEntrada = time;
        
        posicao = status;
        
	}
	
	@PreUpdate
	public void preUpdate() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 0;
        
        dataSaida = dataAtual;
        horaSaida = time;
        
        posicao = status;
        
	}
	
}
