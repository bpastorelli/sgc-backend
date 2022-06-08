package br.com.sgc.validators;

import br.com.sgc.errorheadling.RegistroException;

public interface Validators<T> {
	void validar(T t) throws RegistroException;
}
