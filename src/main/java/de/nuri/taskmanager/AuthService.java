package de.nuri.taskmanager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final UserRepository  repository;
	private final PasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository      = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User register(RegisterRequest request) {
		if (repository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		
		User user = new User(
				request.getUsername(),
				request.getEmail(),
				passwordEncoder.encode(request.getPassword())
		);
		
		return repository.save(user);
	}
	
	public LoginResponse login(LoginRequest request) {
		User user =
				repository.findByEmail(request.getEmail())
				          .orElseThrow(() -> new RuntimeException("Invalid email or password"));
		
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}
		
		return new LoginResponse("dummy-token");
	}
}