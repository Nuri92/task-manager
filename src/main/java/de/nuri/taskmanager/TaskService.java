package de.nuri.taskmanager;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
	
	private final TaskRepository repository;
	
	public TaskService(TaskRepository repository) {
		this.repository = repository;
	}
	
	public List<Task> getTasks() {
		return repository.findAll();
	}
	
	public Task addTask(TaskRequest request) {
		Task task = new Task(request.getTitle(), request.getDescription());
		return repository.save(task);
	}
}
