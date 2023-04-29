package pl.jordii.todoapp.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task's description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup taskGroup;

    public Task() {
    }

    public void updateFrom(final Task sourceTask) {
        description = sourceTask.description;
        done = sourceTask.done;
        deadline = sourceTask.deadline;
        taskGroup = sourceTask.taskGroup;
    }
    public Task(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    TaskGroup getTaskGroup() {
        return taskGroup;
    }

    void setTaskGroup(final TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }
}
