package pl.jordii.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import pl.jordii.todoapp.model.Task;
import pl.jordii.todoapp.repository.TaskRepository;

import java.util.List;

@Repository(value = "todos")
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Long> {

    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from TASKS where ID=:id")
    boolean existsById(@Param("id") Long id);

    @Override
    boolean existsByDoneIsFalseAndTaskGroup_Id(Long groupId);

}
