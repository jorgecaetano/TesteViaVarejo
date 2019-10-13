package com.teste.viaVarejo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa itens de parcelamento
 * 
 * @author Jorge Caetano
 */
@ApiModel(description = "Item do parcelamento do pedido.")
public class Parcel {
	
	@ApiModelProperty(notes = "Número da parcela", required = true, example = "1")
	@JsonProperty("numeroParcela")
	private Integer parcelNumber;
	
	@ApiModelProperty(notes = "Valor da parcela", required = true, example = "99.99")
	@JsonProperty("valor")
	private Float value;
	
	@ApiModelProperty(notes = "Taxa de Juros", required = true, example = "5.6")
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("taxaJurosAoMes")
	private Float monthRate;
	
	public Parcel(Integer parcelNumber, Float value) {
		this.parcelNumber = parcelNumber;
		this.value = value;		
	}
	
	public Parcel(Integer parcelNumber, Float value, Float monthRate) {
		this.parcelNumber = parcelNumber;
		this.value = value;		
		this.monthRate = monthRate;
	}

	public Integer getParcelNumber() {
		return parcelNumber;
	}

	public void setParcelNumber(Integer parcelNumber) {
		this.parcelNumber = parcelNumber;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Float getMonthRate() {
		return monthRate;
	}

	public void setMonthRate(Float monthRate) {
		this.monthRate = monthRate;
	}

}
