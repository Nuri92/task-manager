package de.nuri.taskmanager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
	List<Task> findByOwnerEmail(String email);
	Optional<Task> findByIdAndOwnerEmail(int id, String email);
}
