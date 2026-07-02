package de.nuri.taskmanager;

public class UserResponse {
	
	private int    id;
	private String username;
	private String email;
	
	public UserResponse(User user) {
		this.id       = user.getId();
		this.username = user.getUsername();
		this.email    = user.getEmail();
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
}
