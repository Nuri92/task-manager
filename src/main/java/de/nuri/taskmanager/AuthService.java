package de.nuri.taskmanager;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final UserRepository repository;
	
	public AuthService(UserRepository repository) {
		this.repository = repository;
	}
	
	public User register(RegisterRequest request) {
		if (repository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		
		User user = new User(
				request.getUsername(),
				request.getEmail(),
				request.getPassword()
		);
		
		return repository.save(user);
	}
}