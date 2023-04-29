package pl.jordii.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jordii.todoapp.model.Project;
import pl.jordii.todoapp.repository.ProjectRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long> {

    @Override
    @Query("select distinct p from Project p join fetch p.projectSteps")
    List<Project> findAll();
}
