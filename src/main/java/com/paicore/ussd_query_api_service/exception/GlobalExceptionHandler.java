package com.paicore.ussd_query_api_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
				.toList();

		ErrorResponse body = ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message("Validation failed")
				.path(request.getRequestURI())
				.fieldErrors(fieldErrors)
				.build();

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler({DateTimeParseException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorResponse> handleParseException(
			Exception ex,
			HttpServletRequest request) {
		ErrorResponse body = ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(extractMessage(ex))
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
			IllegalArgumentException ex,
			HttpServletRequest request) {
		ErrorResponse body = ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return ResponseEntity.badRequest().body(body);
	}

	private String extractMessage(Exception ex) {
		if (ex instanceof DateTimeParseException) {
			return "Invalid date format. Expected: yyyy-MM-dd HH:mm:ss";
		}
		String message = ex.getMessage();
		if (message != null && message.contains("LocalDateTime")) {
			return "Invalid date format. Expected: yyyy-MM-dd HH:mm:ss";
		}
		return message != null ? message : "Malformed request body";
	}
}
