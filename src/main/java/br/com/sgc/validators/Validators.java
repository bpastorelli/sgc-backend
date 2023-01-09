package br.com.sgc.validators;

import br.com.sgc.errorheadling.RegistroException;

public interface Validators<T, X> {
	
	void validarPost(T t) throws RegistroException;
	
	void validarPut(X x, Long id) throws RegistroException;
}
