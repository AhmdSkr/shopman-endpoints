package com.example.shopman.advice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.shopman.validation.NegativePageException;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String INVALID_INPUT_RESPONSE_TITLE = "invalid input";
	public static final String SQL_CONSTRAINT_VIOLATION_RESPONSE_TITLE = "constraints violation";

	@Data
	@AllArgsConstructor
	public static class ProblemDetail {
		private LocalDateTime timestamp;
		private String title;
		private Map<String, String> errors;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ProblemDetail> handle(ConstraintViolationException exception, WebRequest request) {
		Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
		Map<String, String> errors = new HashMap<>(violations.size());
		violations.forEach((violation) -> {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		});
		return new ResponseEntity<>(new ProblemDetail(LocalDateTime.now(), INVALID_INPUT_RESPONSE_TITLE, errors),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ProblemDetail> handle(SQLIntegrityConstraintViolationException exception) {
		Map<String, String> errors = new HashMap<>(1);
		errors.put("constraint", exception.getMessage());
		return new ResponseEntity<>(
				new ProblemDetail(LocalDateTime.now(), SQL_CONSTRAINT_VIOLATION_RESPONSE_TITLE, errors),
				HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(NegativePageException.class)
	public ResponseEntity<ProblemDetail> handle(NegativePageException exception) {
		Map<String, String> errors = new HashMap<>(1);
		errors.put("page", "The page index should not be negative");
		return new ResponseEntity<>(
				new ProblemDetail(LocalDateTime.now(), "invalid page index", errors),
				HttpStatus.BAD_REQUEST);
	}
	
}
