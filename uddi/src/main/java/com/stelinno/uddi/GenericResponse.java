package com.stelinno.uddi;

public class GenericResponse {
	public GenericResponse(String message, Object data) {
		this.message = message;
		this.data = data;
	}
	public GenericResponse(Object data) {
		this.data = data;
	}
	public GenericResponse() {

	}
	public String message;
	public Object data;
}
