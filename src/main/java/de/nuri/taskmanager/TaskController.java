package de.nuri.taskmanager;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService service;
	
	public TaskController(TaskService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<Task> getTasks() {
		return service.getTasks();
	}
	
	@PostMapping
	public Task addTask(@Valid @RequestBody TaskRequest request) {
		return service.addTask(request);
	}
}
