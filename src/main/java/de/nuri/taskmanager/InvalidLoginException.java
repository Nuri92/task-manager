package de.nuri.taskmanager;

public class InvalidLoginException extends RuntimeException {
	public InvalidLoginException() {
		super("Invalid email or password");
	}
}
