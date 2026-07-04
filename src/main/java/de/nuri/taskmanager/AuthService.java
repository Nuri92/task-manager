package de.nuri.taskmanager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
	
	private final UserRepository  repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService      jwtService;
	
	public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.repository      = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService      = jwtService;
	}
	
	public User register(RegisterRequest request) {
		if (repository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException();
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
			throw new InvalidLoginException();
		}
		
		String token = jwtService.generateToken(user.getEmail());
		
		return new LoginResponse(token);
	}
	
	public List<UserResponse> getAllUsers() {
		return repository.findAll()
		                 .stream()
		                 .map(UserResponse::new)
		                 .toList();
	}
}