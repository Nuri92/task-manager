package de.nuri.taskmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
	
	@GetMapping("/tasks")
	public String getTasks() {
		return "Protected endpoint";
	}
}
