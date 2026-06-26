package de.nuri.taskmanager;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest {
	
	@NotBlank
	private String title;
	
	private String description;
	
	private TaskRequest() {
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
}
