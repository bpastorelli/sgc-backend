package br.com.sgc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoEnum {
	
	INICIANDO(0),
	IMPORTANDO(1),
	FALHA(2),
	CONCLUIDO(3);
	
	private final int value;
	
	SituacaoEnum(int value){
		this.value = value;
	}
	
	@JsonValue
	public int getValue() {
		return this.value;
	}
	
	@JsonCreator(mode = Mode.DELEGATING)
	public static SituacaoEnum getSituacaoEnum(final int value) {
		
		for(SituacaoEnum situacao : SituacaoEnum.values()) {
			if(situacao.value == value) {
				return situacao;
			}
			
		}
		return null;
	}
	
	public String getDescricaoTextual() {
		
		return this.value + "-" + this.name();
	}
	
}
