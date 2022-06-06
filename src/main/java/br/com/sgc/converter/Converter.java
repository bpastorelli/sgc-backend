package br.com.sgc.converter;

public interface Converter<T, Z> {
	
	T convert(Z object);

}
