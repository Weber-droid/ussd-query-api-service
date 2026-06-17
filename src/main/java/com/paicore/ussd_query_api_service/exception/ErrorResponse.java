package com.paicore.ussd_query_api_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		List<FieldError> fieldErrors
) {

	public ErrorResponse {
		fieldErrors = fieldErrors == null ? null : List.copyOf(fieldErrors);
	}

	public record FieldError(String field, String message) {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private Instant timestamp;
		private int status;
		private String error;
		private String message;
		private String path;
		private List<FieldError> fieldErrors;

		public Builder timestamp(Instant timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder status(int status) {
			this.status = status;
			return this;
		}

		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder fieldErrors(List<FieldError> fieldErrors) {
			this.fieldErrors = fieldErrors == null ? null : List.copyOf(fieldErrors);
			return this;
		}

		public ErrorResponse build() {
			return new ErrorResponse(timestamp, status, error, message, path, fieldErrors);
		}
	}
}
