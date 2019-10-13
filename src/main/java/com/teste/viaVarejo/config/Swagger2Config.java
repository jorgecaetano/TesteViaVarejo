package com.teste.viaVarejo.config;

import java.util.List;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe para configurar a API de documentação Swagger
 * 
 * @author Jorge Caetano
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	/**
	 * Ativar na documentação apenas o status 400 (Bad Request)
	 */
	private List<ResponseMessage> responseMessageForPost()
	{
	    return new ArrayList<ResponseMessage>() {
		{	        
	        add(new ResponseMessageBuilder()
	            .code(400)
	            .message("Bad Request")
	            .build());
	    }};
	}
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()                  
        		.apis(RequestHandlerSelectors
                .basePackage("com.teste.viaVarejo.resources"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .globalResponseMessage(RequestMethod.POST, responseMessageForPost())
                .apiInfo(apiEndPointsInfo());
    }
	
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("REST API Via Varejo")
            .description("REST API de pagamento")
            .contact(new Contact("Jorge Caetano", "", "jorge.caetano4@gmail.com"))
            .version("1.0.0")
            .build();
    }
    
}