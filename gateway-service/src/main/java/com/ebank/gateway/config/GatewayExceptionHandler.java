package com.ebank.gateway.config;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * When no instance of a service is registered in Eureka, the load balancer
 * raises a 503 that would otherwise reach the client as a generic 500.
 */
@RestControllerAdvice
public class GatewayExceptionHandler {

	@ExceptionHandler(HttpStatusCodeException.class)
	public ProblemDetail handle(HttpStatusCodeException e) {
		return ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getStatusText());
	}

}
