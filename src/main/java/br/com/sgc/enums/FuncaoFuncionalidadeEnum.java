package br.com.sgc.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

public enum FuncaoFuncionalidadeEnum {
	
	CREATE("create"),
	EDIT("edit"),
	VIEW("view"),
	DELETE("delete");
	
	public String funcao;
	
	private FuncaoFuncionalidadeEnum(String funcao){
		
		this.funcao = funcao;
		
	}
	
	private static Map<String, FuncaoFuncionalidadeEnum> namesMap;
	
	static {
		cargaValores();
	}
	
	@JsonGetter
	public String getFuncaoCode() {
		
		return this.funcao;
	}
	
	public static void cargaValores() {
		
		namesMap = new HashMap<String, FuncaoFuncionalidadeEnum>();
		
		for(FuncaoFuncionalidadeEnum func : values()) {
			namesMap.put(func.funcao, func);
			namesMap.put(func.funcao.toUpperCase(), func);
		}
	}
	
	@JsonCreator
	public static FuncaoFuncionalidadeEnum forValue(String value) {
		
		return namesMap.get(value);
	}
	
	public static String fromValue(String value) {
		
		return valueOf(value).funcao;
	}
}
