package pl.jordii.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import pl.jordii.todoapp.model.Task;

import java.util.List;

@Repository(value = "todos")
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Long> {

    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from TASKS where ID=:id")
    boolean existsById(@Param("id") Long id);

    @Override
    boolean existsByDoneIsFalseAndTaskGroup_Id(Long groupId);

}
