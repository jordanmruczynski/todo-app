package pl.jordii.todoapp.logic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.jordii.todoapp.config.TaskConfigurationProperties;
import pl.jordii.todoapp.model.Project;
import pl.jordii.todoapp.model.Task;
import pl.jordii.todoapp.model.TaskGroup;
import pl.jordii.todoapp.model.projection.GroupReadModel;
import pl.jordii.todoapp.repository.ProjectRepository;
import pl.jordii.todoapp.repository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private TaskGroupRepository taskGroupRepository;
    private ProjectRepository projectRepository;
    private TaskConfigurationProperties config;


    public ProjectService(final TaskGroupRepository taskGroupRepository, final ProjectRepository projectRepository, @Qualifier("taskConfigurationProperties") final TaskConfigurationProperties config) {
        this.taskGroupRepository = taskGroupRepository;
        this.projectRepository = projectRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, long projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getProjectSteps().stream()
                            .map(step -> new Task(
                                    step.getDescription(),
                                    deadline.plusDays(step.getDaysToDeadline())))
                            .collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
