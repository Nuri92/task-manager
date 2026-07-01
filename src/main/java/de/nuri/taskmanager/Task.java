package de.nuri.taskmanager;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String title;
	
	private String description;
	
	private boolean done;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User owner;
	
	public Task() {
	}
	
	public Task(String title, String description, User owner) {
		this.title       = title;
		this.description = description;
		this.done        = false;
		this.owner       = owner;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void update(String title, String description) {
		this.title       = title;
		this.description = description;
	}
	
	public void markDone() {
		this.done = true;
	}
	
	public User getOwner() {
		return owner;
	}
}
