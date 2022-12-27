package br.com.sgc.dto;

import java.io.Serializable;
import java.sql.Time;

import br.com.sgc.entities.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETVisitaResponseDto implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long    id;
	private String  nome;
	private String  rg;
	private String  cpf;
	private String  dataEntrada;
	private Time    horaEntrada;
	private String  dataSaida;
	private Time    horaSaida;
	private String  endereco;
	private String  numero;
	private String  complemento;
	private String  bairro;
	private String  cidade;
	private String  uf;
	private String  placa;
	private Integer posicao;
	private Veiculo veiculo;
	
}
