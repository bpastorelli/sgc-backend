package br.com.sgc.validators;

import java.util.List;

import br.com.sgc.errorheadling.ErroRegistro;
import br.com.sgc.errorheadling.RegistroException;

public interface Validators<T> {
	List<ErroRegistro> validar(T t) throws RegistroException;
}
