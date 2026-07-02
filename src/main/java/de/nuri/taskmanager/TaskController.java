package de.nuri.taskmanager;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
	public List<Task> getTasks(Authentication authentication) {
		String email = authentication.getName();
		return service.getTasks(email);
	}
	
	@PostMapping
	public Task addTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
		String email = authentication.getName();
		return service.addTask(request, email);
	}
	
	@GetMapping("/{id}")
	public Task getTask(@PathVariable int id, Authentication authentication) {
		return service.getTaskById(id, authentication.getName());
	}
	
	@PutMapping("/{id}")
	public Task updateTask(@PathVariable int id, @RequestBody TaskRequest request, Authentication authentication) {
		return service.updateTask(id, request, authentication.getName());
	}
}
