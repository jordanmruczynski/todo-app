package pl.jordii.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jordii.todoapp.model.TaskGroup;
import pl.jordii.todoapp.repository.TaskGroupRepository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Long> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks")

    List<TaskGroup> findAll();
}
