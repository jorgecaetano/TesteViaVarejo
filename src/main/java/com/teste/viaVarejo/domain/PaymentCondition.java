package com.teste.viaVarejo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Classe que contém informações de condição de pagamento do pedido
 * 
 * @author Jorge Caetano
 */
@ApiModel(description = "Detalhes sobre a condição de pagamento do pedido.")
public class PaymentCondition {
	 
	@ApiModelProperty(notes = "Valor de Entrada", required = false, example = "99.99")
	@JsonProperty("valorEntrada")
	private Float initialPayment;
	
	@ApiModelProperty(notes = "Quantidade de parcelas", required = true, example = "10")
	@JsonProperty("qtdeParcelas")
	private Integer amountParcel;

	public Float getInitialPayment() {
		return initialPayment != null ? initialPayment : 0;
	}

	public void setInitialPayment(Float initialPayment) {
		this.initialPayment = initialPayment;
	}

	public Integer getAmountParcel() {
		return amountParcel;
	}

	public void setAmountParcel(Integer amountParcel) {
		this.amountParcel = amountParcel;
	}

}
