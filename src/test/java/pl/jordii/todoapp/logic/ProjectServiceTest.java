package pl.jordii.todoapp.logic;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.jordii.todoapp.config.TaskConfigurationProperties;
import pl.jordii.todoapp.model.Project;
import pl.jordii.todoapp.model.ProjectSteps;
import pl.jordii.todoapp.model.TaskGroup;
import pl.jordii.todoapp.model.projection.GroupReadModel;
import pl.jordii.todoapp.model.projection.GroupTaskReadModel;
import pl.jordii.todoapp.repository.ProjectRepository;
import pl.jordii.todoapp.repository.TaskGroupRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupsExists_throwsIllegalStateException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService( mockGroupRepository, null, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0l));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(null, mockRepository, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0l));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        // and
        TaskGroupRepository taskGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(null, mockRepository, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0l));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createGroup() {
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project =  projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));
        // and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(inMemoryGroupRepo, mockRepository, mockConfig);

        // when
        GroupReadModel result = toTest.createGroup(today, 1l);
//        assertThat(result)
//                .hasFieldOrPropertyWithValue("description", "bar");
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        // then
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());

    }

    private Project projectWith(String description, Set<Integer> daysToDeadline) {
        Set<ProjectSteps> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectSteps.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(-1);
                    return step;
        }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(description);
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyLong())).thenReturn(result);
        return mockGroupRepository;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private long index = 0;
        private Map<Long, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return map.values().stream().collect(Collectors.toList());
        }

        @Override
        public Optional<TaskGroup> findById(final Long id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Long id) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == id);
        }
    }

}