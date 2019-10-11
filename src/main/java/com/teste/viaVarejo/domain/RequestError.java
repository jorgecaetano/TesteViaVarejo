package com.teste.viaVarejo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Detalhes do erro na requisição.")
public class RequestError {
	
	@ApiModelProperty(notes = "Horário da requisição (no formato UNIX)", example = "1570764017384")
	private Long timestamp;
	
	@ApiModelProperty(notes = "Status da requisição", example = "400")
	private int status;
	
	@ApiModelProperty(notes = "Erro da requisição", example = "Bad Request")
	private String error;
	
	@ApiModelProperty(notes = "Mensagem detalhada do erro na requisição", example = "Se for informado, o valor de entrada não pode ser maior do que o valor do produto")
	private String message;

	@ApiModelProperty(notes = "Caminho do recurso que gerou o erro na requisição", example = "/via_varejo/api/order")
	private String path;

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
