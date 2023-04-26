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

    public void updateFrom(final Task sourceTask) {
        description = sourceTask.description;
        done = sourceTask.done;
        deadline = sourceTask.deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
