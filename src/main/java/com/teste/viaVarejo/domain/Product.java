package com.teste.viaVarejo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Classe que contém informações dos produtos alvo de solicitação de pagamento
 * 
 * @author Jorge Caetano
 */
@ApiModel(description = "Produtos alvo da solicitação de parcelamento de pedido.")
public class Product {
	
	@ApiModelProperty(notes = "Código do produto", required = true, example = "123")
	@JsonProperty("codigo")
	private Long code;	
	
	@ApiModelProperty(notes = "Nome do produto", required = true, example = "TV")
	@JsonProperty("nome")
	private String name;
	
	@ApiModelProperty(notes = "Valor do produto", required = true, example = "999.99")
	@JsonProperty("valor")
	private Float value;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}	

}
