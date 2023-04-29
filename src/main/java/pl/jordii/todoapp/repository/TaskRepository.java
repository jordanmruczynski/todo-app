package pl.jordii.todoapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import pl.jordii.todoapp.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task save(Task entity);

    Page<Task> findAll(Pageable pageable);

    List<Task> findByDone(@Param("status") boolean done);

    boolean existsById(Long id);

    boolean existsByDoneIsFalseAndTaskGroup_Id(Long groupId);

}
