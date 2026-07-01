package de.nuri.taskmanager;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	private String email;
	private String password;
	
	
	public User() {
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email    = email;
		this.password = password;
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
	
	public String getPassword() {
		return password;
	}
	
}
