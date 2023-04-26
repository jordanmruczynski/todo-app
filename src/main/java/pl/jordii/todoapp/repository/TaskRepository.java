package pl.jordii.todoapp.repository;

import pl.jordii.todoapp.model.Task;

import java.util.List;
import java.util.Optional;

public interface TakRepository {
    List<Task> findAll();
    Optional<Task> findById(Long id);
    Task save(Task entity);



}
