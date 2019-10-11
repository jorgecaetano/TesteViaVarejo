package com.teste.viaVarejo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teste.viaVarejo.domain.Order;
import com.teste.viaVarejo.domain.Parcel;
import com.teste.viaVarejo.exception.InvalidOrderRequestException;
import com.teste.viaVarejo.exception.OrderValueZeroException;

@Service
public class OrderService {
	
	private Float RATE_SELIC = 1.15F;
	
	private void validateOrder(Order order) {
		if (order.getPaymentCondition() == null)
			throw new InvalidOrderRequestException("A condição de pagamento é de preenchimento obrigatório");
		
		if (order.getPaymentCondition().getAmountParcel() == null)
			throw new InvalidOrderRequestException("A quantidade de parcelas da condição de pagamento é de preenchimento obrigatório");
		
		if (order.getPaymentCondition().getAmountParcel() < 1)
			throw new InvalidOrderRequestException("A quantidade de parcelas da condição de pagamento tem que ser maior do que 0 (zero)");
		
		if (order.getPaymentCondition().getInitialPayment() != null && order.getPaymentCondition().getInitialPayment() < 0)
			throw new InvalidOrderRequestException("Se for informado, o valor de entrada não pode ser menor do que 0 (zero)");		
		
		if (order.getProduct() == null)
			throw new InvalidOrderRequestException("O produto é de preenchimento obrigatório");
		
		if (order.getProduct().getCode() == null)
			throw new InvalidOrderRequestException("O código do produto é de preenchimento obrigatório");
		
		if (order.getProduct().getCode() < 1)
			throw new InvalidOrderRequestException("O código do produto tem que ser maior do que 0 (zero)");
		
		if (order.getProduct().getName() == null || order.getProduct().getName().isEmpty())
			throw new InvalidOrderRequestException("O nome do produto é de preenchimento obrigatório");
		
		if (order.getProduct().getValue() == null)
			throw new InvalidOrderRequestException("O valor do produto é de preenchimento obrigatório");
		
		if (order.getProduct().getValue() < 1)
			throw new InvalidOrderRequestException("O valor do produto tem que ser maior do que 0 (zero)");
		
		if (order.getPaymentCondition().getInitialPayment() != null && (order.getProduct().getValue() < order.getPaymentCondition().getInitialPayment()))
			throw new InvalidOrderRequestException("Se for informado, o valor de entrada não pode ser maior do que o valor do produto");
		
		if (order.getProduct().getValue().equals(order.getPaymentCondition().getInitialPayment()))
			throw new OrderValueZeroException("Como o valor de entrada é igual ao valor do produto, o pedido já estará pago");
		
	}
	
	public Float calculateTotalFinalOrderValue(Integer amountParcels, Float orderValue) {
		return orderValue + (orderValue * ((this.RATE_SELIC / 100) * amountParcels)); 
	}
	
	public List<Parcel> process(Order order){
		this.validateOrder(order);
		
		Float finalOrderValue = 0F;
		
		if (order.getPaymentCondition().getAmountParcel() < 6) 
			finalOrderValue = order.getProduct().getValue() - order.getPaymentCondition().getInitialPayment();
		else
			finalOrderValue = this.calculateTotalFinalOrderValue(
					order.getPaymentCondition().getAmountParcel(), 
					order.getProduct().getValue() - order.getPaymentCondition().getInitialPayment());
			
		
		Float parcelValue = finalOrderValue / order.getPaymentCondition().getAmountParcel();
		
		List<Parcel> Parcels = new ArrayList<Parcel>();
		
		for (int i = 1 ; i <= order.getPaymentCondition().getAmountParcel() ; i++)
			Parcels.add(new Parcel(i, parcelValue));
		
		return Parcels;
	}

}
