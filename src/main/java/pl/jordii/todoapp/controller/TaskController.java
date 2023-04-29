package pl.jordii.todoapp.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.jordii.todoapp.model.Task;
import pl.jordii.todoapp.repository.TaskRepository;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/todos")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/todos")
   // @GetMapping(value = "/todos", params = {"!sort", "!page", "!size"})
    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        logger.warn("Exposing all the tasks pageable!");
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    ResponseEntity<Task> readTask(@PathVariable Long id) {
        logger.warn("Exposing the task with id: " + id);
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        Task result = repository.save(task);
        logger.warn("Saving the task");
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

//    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
//    ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody @Valid Task updatedTask) {
//        if (!repository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        updatedTask.setId(id);
//        repository.save(updatedTask);
//        logger.warn("Saving the task");
//        return ResponseEntity.noContent().build();
//    }

    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    ResponseEntity<?> toggleTask(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(t -> t.setDone(!t.isDone()));
        logger.warn("Saving the task");
        return ResponseEntity.noContent().build();
    }

}
