package pl.jordii.todoapp.repository;

import pl.jordii.todoapp.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Long id);

    Project save(Project entity);

}
