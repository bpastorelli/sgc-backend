package br.com.sgc.dto;

import java.io.Serializable;

import javax.persistence.Transient;

import br.com.sgc.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaMoradorDto implements Serializable {

	private static final long serialVersionUID = -5754246207015712518L;
	
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String rg;
	
	private String telefone;
	
	private String celular;
	
	private PerfilEnum perfil;
	
	private Long residenciaId;
	
	private Long associado;
	
	private Long posicao;
	
	@Transient
	private String guide;

}
