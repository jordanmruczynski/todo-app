package pl.jordii.todoapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.jordii.todoapp.model.Task;
import pl.jordii.todoapp.model.TaskGroup;
import pl.jordii.todoapp.model.projection.GroupReadModel;
import pl.jordii.todoapp.model.projection.GroupWriteModel;
import pl.jordii.todoapp.repository.TaskGroupRepository;
import pl.jordii.todoapp.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(long groupId) {
        if (taskRepository.existsByDoneIsFalseAndTaskGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. You need done all the task first.");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }

}
