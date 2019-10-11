package com.teste.viaVarejo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Detalhes sobre a solicitação de parcelamento de pedido.")
public class Order {
	
	@JsonProperty("produto")
	@ApiModelProperty(notes = "Dados do produto a ser processado", required = true)
	private Product product;
	
	@JsonProperty("condicaoPagamento")	
	@ApiModelProperty(notes = "Condição de pagamento do pedido", required = true)
	private PaymentCondition paymentCondition;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PaymentCondition getPaymentCondition() {
		return paymentCondition;
	}

	public void setPaymentCondition(PaymentCondition paymentCondition) {
		this.paymentCondition = paymentCondition;
	}	

}
