package com.teste.viaVarejo.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.viaVarejo.domain.Order;
import com.teste.viaVarejo.domain.Parcel;
import com.teste.viaVarejo.domain.RequestError;
import com.teste.viaVarejo.services.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/order")
@Api(value="Recurso para processamento de pagamentos")
public class OrderResource {
	
	@Autowired
	private OrderService service;	
	
	@ApiOperation(value = "Executa o processamento de pagamento em parcelas", response = Parcel.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucesso no processamento de parcelas"),
		    @ApiResponse(code = 400, message = "Dados de processamento de parcelas incorretos", response = RequestError.class)		    		    
		})
	@PostMapping
	public ResponseEntity<List<Parcel>> processOrder(
			@ApiParam(value = "Objeto de processamento de pagamento em parcelas", required = true) @RequestBody Order order) {
		
		List<Parcel> parcels = this.service.process(order);
		return ResponseEntity.ok().body(parcels);
	}

}
