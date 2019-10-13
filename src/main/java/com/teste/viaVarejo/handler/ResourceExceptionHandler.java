package com.teste.viaVarejo.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.teste.viaVarejo.domain.RequestError;
import com.teste.viaVarejo.exception.InvalidOrderRequestException;
import com.teste.viaVarejo.exception.OrderValueZeroException;

/**
 * Classe para interceptar e tratar todas as exceções lançadas a partir da
 * camada REST do Spring
 *
 * @author Jorge Caetano
 */
@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = LogManager.getLogger(ResourceExceptionHandler.class.getName());
	private Gson gson = new Gson();

	/**
	 * Monta um objeto contendo o detalhamento do erro ocorrido para ser devolvido
	 * na requisição.
	 * 
	 * @param shortMessage    Mensagem resumida do erro
	 * @param detailedMessage Mensagem detalhada do erro
	 * @param httpStatus      Status HTTP a ser respondido (4XX/5XX)
	 * @return {@link RequestError @RequestError}
	 */
	private RequestError buildRequestError(String detailedMessage, HttpStatus httpStatus, String path) {

		String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().scheme("").host("").port(null).toUriString().replace("://", "");
		
		RequestError requestError = new RequestError();
		requestError.setTimestamp(new Date().getTime());
		requestError.setError(httpStatus.getReasonPhrase());
		requestError.setMessage(detailedMessage);
		requestError.setStatus(httpStatus.value());
		requestError.setPath(uri);		

		this.logger.error(String.format("Erro na requisição HTTPStatus: [%s] Retorno: [%s] ", httpStatus.toString(),
				this.gson.toJson(requestError)));

		return requestError;
	}

	@ExceptionHandler(OrderValueZeroException.class)
	public ResponseEntity<RequestError> handleOrderValueZeroException(
			OrderValueZeroException ex, HttpServletRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(requestError);
	}
	
	@ExceptionHandler(InvalidOrderRequestException.class)
	public ResponseEntity<RequestError> handleInvalidOrderRequestException(
			InvalidOrderRequestException ex, HttpServletRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(requestError);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<RequestError> handleException(Exception ex, HttpServletRequest request) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(requestError);
	}	

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		
		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.NOT_FOUND;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}

	@Override
	protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;

		RequestError requestError = this.buildRequestError(ex.getMessage(), httpStatus, request.getContextPath());
		return handleExceptionInternal(ex, requestError, headers, httpStatus, request);
	}
	
}
