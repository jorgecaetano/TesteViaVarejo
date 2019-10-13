package com.teste.viaVarejo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teste.viaVarejo.domain.Order;
import com.teste.viaVarejo.domain.Parcel;
import com.teste.viaVarejo.exception.InvalidOrderRequestException;
import com.teste.viaVarejo.exception.OrderValueZeroException;

/**
 * Classe que que contém todo o tratamento para execução de parcelamento/pagamento de pedido
 * @author Jorge Caetano
 */
@Service
public class OrderService {

	public Float RATE_SELIC = 1.15F;

	private Integer PARCEL_TO_RATE = 6;
	
	/**
	 * Executa as validações necessárias para o pedido enviado na requisição
	 * @param order ({@link Order @Order}) Pedido enviado na requisição    
	 */
	private void validateOrder(Order order) {
		if (order.getPaymentCondition() == null)
			throw new InvalidOrderRequestException("A condição de pagamento é de preenchimento obrigatório");

		if (order.getPaymentCondition().getAmountParcel() == null)
			throw new InvalidOrderRequestException(
					"A quantidade de parcelas da condição de pagamento é de preenchimento obrigatório");

		if (order.getPaymentCondition().getAmountParcel() < 1)
			throw new InvalidOrderRequestException(
					"A quantidade de parcelas da condição de pagamento tem que ser maior do que 0 (zero)");

		if (order.getPaymentCondition().getInitialPayment() != null
				&& order.getPaymentCondition().getInitialPayment() < 0)
			throw new InvalidOrderRequestException(
					"Se for informado, o valor de entrada não pode ser menor do que 0 (zero)");

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

		if (order.getProduct().getValue() <= 0)
			throw new InvalidOrderRequestException("O valor do produto tem que ser maior do que 0 (zero)");

		if (order.getPaymentCondition().getInitialPayment() != null
				&& (order.getProduct().getValue() < order.getPaymentCondition().getInitialPayment()))
			throw new InvalidOrderRequestException(
					"Se for informado, o valor de entrada não pode ser maior do que o valor do produto");

		if (order.getProduct().getValue().equals(order.getPaymentCondition().getInitialPayment()))
			throw new OrderValueZeroException(
					"Como o valor de entrada é igual ao valor do produto, o pedido já estará pago");

	}
	
	/**
	 * Processa o parcelamento do pedido considerando a taxa Selic
	 * @param orderValue Valor do pedido
	 * @param amountParcels Quantidade de parcelas
	 * @param selicRate Taxa Selic considerada
	 * @return List<Parcel> ({@link Parcel @Parcel}) Lista de parcelas    
	 */
	public List<Parcel> getParcelsToOrder(Float orderValue, Integer amountParcels, Float selicRate){
				
		Float finalOrderValue = orderValue + (orderValue * ((selicRate / 100) * amountParcels));

		Float parcelValue = finalOrderValue / amountParcels;
		
		List<Parcel> parcels = new ArrayList<Parcel>();

		for (int i = 1; i <= amountParcels; i++)
			parcels.add(new Parcel(i, parcelValue, selicRate));
		
		return parcels;
	}
	
	/**
	 * Processa o parcelamento do pedido sem considerar a taxa Selic
	 * @param orderValue Valor do pedido
	 * @param amountParcels Quantidade de parcelas
	 * @return List<Parcel> ({@link Parcel @Parcel}) Lista de parcelas    
	 */
	public List<Parcel> getParcelsToOrder(Float orderValue, Integer amountParcels){
		Float parcelValue = orderValue / amountParcels;

		List<Parcel> parcels = new ArrayList<Parcel>();

		for (int i = 1; i <= amountParcels; i++)
			parcels.add(new Parcel(i, parcelValue));
		
		return parcels;		
	}

	/**
	 * Método principal para executar o parcelamento/pagamento de um pedido
	 * @param order ({@link Order @Order}) Pedido enviado na requisição 
	 * @return List<Parcel> ({@link Parcel @Parcel}) Lista de parcelas   
	 */
	public List<Parcel> process(Order order) {
		this.validateOrder(order);		
		
		Float orderValue = order.getProduct().getValue() - order.getPaymentCondition().getInitialPayment();
		
		if (order.getPaymentCondition().getAmountParcel() < PARCEL_TO_RATE) 
			return this.getParcelsToOrder(orderValue, order.getPaymentCondition().getAmountParcel());
		else
			return this.getParcelsToOrder(orderValue, order.getPaymentCondition().getAmountParcel(), RATE_SELIC);
		
	}

}
