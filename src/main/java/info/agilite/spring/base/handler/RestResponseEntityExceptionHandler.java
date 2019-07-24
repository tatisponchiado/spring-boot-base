package info.agilite.spring.base.handler;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import info.agilite.spring.base.ValidationException;
/**
 * Handler para exceptions, retorna no body da requisição um JSON gerado a partir da classe {@link ExceptionEntity}
 * 
 * 
 * @author Rafael
 *
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler  {
	@ResponseBody
	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<Object> handleResponseStatusException(ValidationException ex, WebRequest request) {
		ex.printStackTrace();//FIXME log e send server error
		return handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, request);
	}
	
	@ResponseBody
	@ExceptionHandler(RestClientResponseException.class)
	protected ResponseEntity<Object> handleResponseStatusException(RestClientResponseException ex, WebRequest request) {
		ex.printStackTrace();//FIXME log e send server error
		return handleExceptionInternal(ex, null, null, HttpStatus.valueOf(ex.getRawStatusCode()),  request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Violation> violations = ex.getBindingResult().getFieldErrors().stream().map(v -> new Violation(v.getField(), v.getDefaultMessage())).collect(Collectors.toList());
		return ResponseEntity.badRequest().body(createMessageError(ex, request, violations)); 
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException e, WebRequest request) {
		List<Violation> violations = e.getConstraintViolations().stream().map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage())).collect(Collectors.toList());
		return ResponseEntity.badRequest().body(createMessageError(e, request, violations)); 
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if(body == null) {
			body = createMessageError(ex, request);
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private ExceptionEntity createMessageError(Exception ex, WebRequest request){
		return createMessageError(ex, request, null);
	}
	
	private ExceptionEntity createMessageError(Exception ex, WebRequest request, List<Violation> validations){
		return new ExceptionEntity(validations != null ? "BadRequest" : ex.getMessage(), ex.getClass().getName(), parseDescription(request), validations);
	}
	
	private String parseDescription(WebRequest request) {
		try {
			String desc = request.getDescription(false);
			return desc.replace("uri=", "");
		} catch (Exception e) {
			return request.getDescription(false);
		}
	}
}
