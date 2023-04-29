package pl.jordii.todoapp.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> taskGroups;
    @OneToMany(mappedBy = "project")
    private Set<ProjectSteps> projectSteps;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    void setTaskGroups(final Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public Set<ProjectSteps> getProjectSteps() {
        return projectSteps;
    }

    void setProjectSteps(final Set<ProjectSteps> projectSteps) {
        this.projectSteps = projectSteps;
    }
}
