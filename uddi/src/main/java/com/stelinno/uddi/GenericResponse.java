package com.stelinno.uddi;

public class GenericResponse {
	public GenericResponse(int status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public int status;
	public String message;
	public Object data;
}
