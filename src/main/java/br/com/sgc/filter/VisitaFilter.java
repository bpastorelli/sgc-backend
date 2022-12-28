package br.com.sgc.filter;

import java.time.LocalDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitaFilter {

	private Long id;
	
	private String nome;
	
	private String rg;
	
	private String cpf;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataFim;
	
	private Integer posicao;
	
	private String guide;
	
	private int pag;
	
	private String ord;
	
	private String dir;
	
	private int size;
	
	private boolean content;
	
}
