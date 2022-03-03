package com.online.bookstore.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.online.bookstore.commons.BookStoreException;
import com.online.bookstore.enums.BookStoreErrors;
import com.online.bookstore.json.ErrorObj;

@ControllerAdvice
@RestController
public class BookStoreErrorController extends ResponseEntityExceptionHandler implements ErrorController {

	public static final Logger cstmlogger = LoggerFactory.getLogger(BookStoreErrorController.class);

	private static final String PATH = "/error";
	private static final String CUSTOM_ERROR_EXCEPTION_ATTRIBUTE = "custom.error.exception.attribute";
	private static final String CUSTOM_ERROR_MESSAGE_ATTRIBUTE = "custom.error.message.attribute";
	private static final String CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE = "custom.error.status.code.attribute";

	@Autowired
	private ErrorAttributes errorAttributes;

	@RequestMapping(value = PATH)
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorObj> error(WebRequest webRequest, HttpServletResponse response, Exception e) {
		ResponseEntity<ErrorObj> out = null;

		Set<Include> includes = new HashSet<>();
		includes.add(Include.BINDING_ERRORS);
		includes.add(Include.EXCEPTION);
		includes.add(Include.MESSAGE);
		includes.add(Include.STACK_TRACE);
		ErrorAttributeOptions.of(includes);
		Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest,
				ErrorAttributeOptions.of(includes));

		String message = (String) errorAttributes.get("message");

		response.setHeader("content-type", "application/json");
		cstmlogger.error("ERROR: {}, HttpStatus: {}", message, response.getStatus());
		Exception validationException = (Exception) webRequest.getAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE,
				WebRequest.SCOPE_SESSION);
		if (validationException != null) {
			HttpStatus status = (HttpStatus) webRequest.getAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE,
					WebRequest.SCOPE_SESSION);
			String errorMsg = (String) webRequest.getAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE,
					WebRequest.SCOPE_SESSION);
			webRequest.removeAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, WebRequest.SCOPE_SESSION);
			webRequest.removeAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION);
			webRequest.removeAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, WebRequest.SCOPE_SESSION);
			out = new ResponseEntity<>(new ErrorObj(BookStoreErrors.VALIDATION_ERROR.getCode(), errorMsg), status);
		} else if (HttpServletResponse.SC_UNAUTHORIZED == response.getStatus()) {
			out = new ResponseEntity<>(new ErrorObj(BookStoreErrors.UNAUTHORIZED.getCode(), message),
					HttpStatus.UNAUTHORIZED);
		} else if (e instanceof BookStoreException) {
			out = new ResponseEntity<>(new ErrorObj(((BookStoreException) e).getCode(), e.getMessage()),
					HttpStatus.BAD_REQUEST);
			response.setContentType("application/json");
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else if (e instanceof RuntimeException) {
			out = new ResponseEntity<>(new ErrorObj(((RuntimeException) e).getCause(), e.getMessage()),
					HttpStatus.BAD_REQUEST);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			out = new ResponseEntity<>(
					new ErrorObj(BookStoreErrors.UNAUTHORIZED.getCode(), HttpStatus.UNAUTHORIZED.getReasonPhrase()),
					HttpStatus.UNAUTHORIZED);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		cstmlogger.info("OUT ERROR END: {}", out.getBody());
		return out;
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		ErrorObj erroreSW = new ErrorObj(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorObj erroreSW = new ErrorObj(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorObj erroreSW = new ErrorObj(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String errorMessage = StringUtils.EMPTY;
		MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getFieldErrors()))
			errorMessage += getFieldErrorMsg(ex.getBindingResult().getFieldErrors());
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getGlobalErrors()))
			errorMessage += getGlobalErrorMsg(ex.getBindingResult().getGlobalErrors());
		ErrorObj erroreSW = new ErrorObj(HttpStatus.BAD_REQUEST.value(), errorMessage);
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String errorMessage = StringUtils.EMPTY;
		BindException ex = (BindException) exception;
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getFieldErrors()))
			errorMessage += getFieldErrorMsg(ex.getBindingResult().getFieldErrors());
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getGlobalErrors()))
			errorMessage += getGlobalErrorMsg(ex.getBindingResult().getGlobalErrors());
		ErrorObj erroreSW = new ErrorObj(HttpStatus.BAD_REQUEST.value(), errorMessage);
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorObj erroreSW = new ErrorObj(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
		request.setAttribute(CUSTOM_ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.METHOD_NOT_ALLOWED,
				WebRequest.SCOPE_SESSION);
		request.setAttribute(CUSTOM_ERROR_MESSAGE_ATTRIBUTE, erroreSW.getMessage(), WebRequest.SCOPE_SESSION);
		return new ResponseEntity<>((Object) erroreSW, HttpStatus.METHOD_NOT_ALLOWED);
	}

	private String getFieldErrorMsg(List<FieldError> fieldErrorList) {
		StringBuilder errorMessage = new StringBuilder("Validation error on fields: ");
		for (FieldError fieldError : fieldErrorList) {
			cstmlogger.error("VALIDATION ERROR ON FIELD {}: {}", fieldError.getField(), fieldError.getDefaultMessage());
			errorMessage.append(fieldError.getDefaultMessage()).append(", ");
		}
		return errorMessage.delete(errorMessage.length() - 2, errorMessage.length()).toString();
	}

	private String getGlobalErrorMsg(List<ObjectError> objectErrorList) {
		StringBuilder errorMessage = new StringBuilder("Validation error on object: ");
		for (ObjectError objectError : objectErrorList) {
			cstmlogger.error("VALIDATION ERROR ON OBJECT {}: {}", objectError.getObjectName(),
					objectError.getDefaultMessage());
			errorMessage.append(objectError.getDefaultMessage()).append(", ");
		}
		return errorMessage.delete(errorMessage.length() - 2, errorMessage.length()).toString();
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
