package de.nuri.taskmanager;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
	
	private final TaskRepository repository;
	private final UserRepository userRepository;
	
	public TaskService(TaskRepository repository, UserRepository userRepository) {
		this.repository     = repository;
		this.userRepository = userRepository;
	}
	
	public List<Task> getTasks(String email) {
		return repository.findByOwnerEmail(email);
	}
	
	public Task addTask(TaskRequest request, String email) {
		User user = userRepository.findByEmail(email)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		
		Task task = new Task(request.getTitle(), request.getDescription(), user);
		return repository.save(task);
	}
	
	public Task getTaskById(int id, String email) {
		return repository.findByIdAndOwnerEmail(id, email)
		                 .orElseThrow(() -> new RuntimeException("Task not found"));
	}
	
	
	public Task updateTask(int id, TaskRequest request, String email) {
		Task task = getTaskById(id, email);
		task.update(request.getTitle(), request.getDescription());
		return repository.save(task);
	}
	
	public void deleteTask(int id, String email) {
		Task task = getTaskById(id, email);
		repository.delete(task);
	}
	
	
	public Task toggleDone(int id, String email) {
		Task task = getTaskById(id, email);
		task.toggleDone();
		return repository.save(task);
	}
}
