package de.nuri.taskmanager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleTaskNotFound(TaskNotFoundException exception) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ApiErrorResponse(
						404,
						exception.getMessage()
				));
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(
			UserAlreadyExistsException exception
	) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new ApiErrorResponse(
						409,
						exception.getMessage()
				));
	}
	
	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidLogin(
			InvalidLoginException exception
	) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiErrorResponse(
						401,
						exception.getMessage()
				));
	}
}
