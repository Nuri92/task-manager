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
	
	public Task() {
	}
	
	public Task(String title, String description) {
		this.title       = title;
		this.description = description;
		this.done        = false;
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
}
