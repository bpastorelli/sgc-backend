package br.com.sgc.response;

import java.util.ArrayList;
import java.util.List;

import br.com.sgc.errorheadling.ErroRegistro;

public class Response<T> {

	private T data;
	private List<ErroRegistro> errors;

	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<ErroRegistro> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<ErroRegistro>();
		}
		return errors;
	}

	public void setErrors(List<ErroRegistro> errors) {
		this.errors = errors;
	}

}
