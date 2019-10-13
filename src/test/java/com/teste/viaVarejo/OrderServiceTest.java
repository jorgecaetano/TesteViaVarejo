package com.teste.viaVarejo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.teste.viaVarejo.domain.Order;
import com.teste.viaVarejo.domain.Parcel;
import com.teste.viaVarejo.domain.PaymentCondition;
import com.teste.viaVarejo.domain.Product;
import com.teste.viaVarejo.exception.InvalidOrderRequestException;
import com.teste.viaVarejo.exception.OrderValueZeroException;
import com.teste.viaVarejo.services.OrderService;

/**
 * Classe para testar o serviço com as regras de negócio da API de parcelamento de pedido
 * @author Jorge Caetano
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesteViaVarejoApplication.class)
public class OrderServiceTest {
	
	@Autowired
	public OrderService orderService;
	
	/**
	 * Teste para garantir a validação de quantidade de parcelas
	 */
	@Test
	public void mustHaveSpecificAmountParcels() {
		
		// Montando cenário
		Exception exception = null;
		
		Integer amountParcels = 5;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(120F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(amountParcels);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		List<Parcel> parcels = null;
		
		// Executa a operação
		try {
			parcels = orderService.process(order);
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(exception).isNull();
		assertThat(parcels).isNotNull();
		assertThat(parcels.size()).isEqualTo(amountParcels);
		
	}
	
	/**
	 * Teste para garantir a validação de calculo com taxa SELIC
	 */
	@Test
	public void mustHaveMonthRate() {
		
		// Montando cenário
		Exception exception = null;
		
		Integer amountParcels = 6;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(120F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(amountParcels);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		List<Parcel> parcels = null;
		
		// Executa a operação
		try {
			parcels = orderService.process(order);
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(exception).isNull();
		assertThat(parcels).isNotNull();
		parcels.forEach(item -> assertThat(item.getMonthRate()).isNotNull());
		assertThat(parcels.size()).isEqualTo(amountParcels);
		
	}
	
	/**
	 * Teste para garantir a validação do valor das parcelas sem taxa SELIC
	 */
	@Test
	public void mustHaveAValidValueWithoutMounthRate() {
		
		// Montando cenário
		Exception exception = null;
		
		Integer amountParcels = 5;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(130F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(amountParcels);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		List<Parcel> parcels = null;
		
		// Executa a operação
		try {
			parcels = orderService.process(order);
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(exception).isNull();
		assertThat(parcels).isNotNull();
		parcels.forEach(item -> assertThat(item.getValue()).isEqualTo(24F));
		assertThat(parcels.size()).isEqualTo(amountParcels);
		
	}
	
	/**
	 * Teste para garantir a validação do valor das parcelas com taxa SELIC
	 */
	@Test
	public void mustHaveAValidValueWithMounthRate() {
		
		// Montando cenário
		Exception exception = null;
		
		Integer amountParcels = 6;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(132F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(amountParcels);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		List<Parcel> parcels = null;
		
		// Executa a operação
		try {
			parcels = orderService.process(order);
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(exception).isNull();
		assertThat(parcels).isNotNull();
		parcels.forEach(item -> {
			assertThat(item.getValue()).isEqualTo(21.736334F);
			assertThat(item.getMonthRate()).isEqualTo(orderService.RATE_SELIC);
		});
		assertThat(parcels.size()).isEqualTo(amountParcels);
		
	}
	
	/**
	 * Teste para garantir a validação de condição de pagamento não informada
	 */
	@Test
	public void mustThrowsExceptionByPaymentConditionNotInformed() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;	
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		Order order = new Order();
		
		order.setPaymentCondition(null);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("A condição de pagamento é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de quantidade de parcelas não informada
	 */
	@Test
	public void mustThrowsExceptionByAmountParcelNotInformed() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;	
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("A quantidade de parcelas da condição de pagamento é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de quantidade de parcelas maior que zero
	 */
	@Test
	public void mustThrowsExceptionByAmountParcelLessThenOne() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;	
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(0);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("A quantidade de parcelas da condição de pagamento tem que ser maior do que 0 (zero)");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de pagamento incial negativo
	 */
	@Test
	public void mustThrowsExceptionByInitialPaymentNegative() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;	
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(-1F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("Se for informado, o valor de entrada não pode ser menor do que 0 (zero)");
		assertThat(exception).isNull();
		
	}

	/**
	 * Teste para garantir a validação de produto não informado
	 */
	@Test
	public void mustThrowsExceptionByProductNotInformed() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(null);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O produto é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de código de produto não informado
	 */
	@Test
	public void mustThrowsExceptionByProductCodeNotInformed() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O código do produto é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de código de produto maior do que zero
	 */
	@Test
	public void mustThrowsExceptionByProductCodeLessThenOne() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(0L);
		product.setName("TV de plasma");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O código do produto tem que ser maior do que 0 (zero)");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de nome de produto nulo
	 */
	@Test
	public void mustThrowsExceptionByProductNameNull() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName(null);
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O nome do produto é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de nome de produto vazio
	 */
	@Test
	public void mustThrowsExceptionByProductNameEmpty() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("");
		product.setValue(1000F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O nome do produto é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de valor de produto não informado
	 */
	@Test
	public void mustThrowsExceptionByProductValueNotInformed() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(null);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O valor do produto é de preenchimento obrigatório");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de valor de produto maior que zero
	 */
	@Test
	public void mustThrowsExceptionByProductValueGreaterThanZero() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(0F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(10F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("O valor do produto tem que ser maior do que 0 (zero)");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de valor de entrada maior que o valor do produto
	 */
	@Test
	public void mustThrowsExceptionByInitialPaymentGreaterThanProductValue() {
		
		// Montando cenário		
		InvalidOrderRequestException invalidOrderRequestException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(120F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(121F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (InvalidOrderRequestException iE) {
			invalidOrderRequestException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(invalidOrderRequestException).isNotNull();
		assertThat(invalidOrderRequestException.getMessage()).isEqualTo("Se for informado, o valor de entrada não pode ser maior do que o valor do produto");
		assertThat(exception).isNull();
		
	}
	
	/**
	 * Teste para garantir a validação de valor de saldo do pedido igual a zero
	 */
	@Test
	public void mustThrowsExceptionByFinalOrderValueIsZero() {
		
		// Montando cenário		
		OrderValueZeroException orderValueZeroException = null;
		Exception exception = null;
		
		Product product = new Product();
		product.setCode(123L);
		product.setName("TV de plasma");
		product.setValue(120F);
		
		PaymentCondition paymentCondition = new PaymentCondition();
		paymentCondition.setAmountParcel(10);
		paymentCondition.setInitialPayment(120F);
		
		Order order = new Order();
		
		order.setPaymentCondition(paymentCondition);
		order.setProduct(product);
		
		// Executa a operação
		try {
			orderService.process(order);
		} catch (OrderValueZeroException iE) {
			orderValueZeroException = iE;
		} catch (Exception e) {
			exception = e;
		}
		
		// Executa o teste
		assertThat(orderValueZeroException).isNotNull();
		assertThat(orderValueZeroException.getMessage()).isEqualTo("Como o valor de entrada é igual ao valor do produto, o pedido já estará pago");
		assertThat(exception).isNull();
		
	}

}
