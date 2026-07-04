package de.nuri.taskmanager;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException() {
		super("Email already exists");
	}
}
