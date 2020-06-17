package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.ProjectStep;
import bartosz.szablewski.todoapp.model.TaskGroups;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.repository.ProjectRepository;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group")
    void createGroup_noMultipleGroupsConfig_And_UndoneGroupExists_ThrowsIllegalStateException() {
        //given
        TaskGroupsRepository mockGroupRepository = getGroupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mockConfig = getConfigurationPropertiesReturning(false);
        //system under test
        var toTest = new ProjectService(mockGroupRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for given id")
    void createGroup_ConfigurationOk_And_NoProjects_ThrowsIllegalArgumentException() {
        //given
        TaskConfigurationProperties mockConfig = getConfigurationPropertiesReturning(true);
        //and
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new ProjectService(null, mockProjectRepository, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for given id")
    void createGroup_noMultipleGroupsConfig_And_oUndoneGroupExists_And_NoProjects_ThrowsIllegalArgumentException() {
        //given
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        InMemoryGroupRepository inMemoryGroupRepository = new InMemoryGroupRepository();
        //and
        TaskConfigurationProperties mockConfig = getConfigurationPropertiesReturning(true);
        //system under test
        var toTest = new ProjectService(inMemoryGroupRepository, mockProjectRepository, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");
    }

    @Test
    @DisplayName("should create new group in project")
    void createGroup_configurationOk_existingProject_createsSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1, -2));
        //and
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepository = new InMemoryGroupRepository();
        var countBeforeCall = inMemoryGroupRepository.count();
        //and
        TaskConfigurationProperties mockConfig = getConfigurationPropertiesReturning(true);
        //system under start
        var toTest = new ProjectService(inMemoryGroupRepository, mockProjectRepository, mockConfig);
        //when
        GroupReadDTO result = toTest.createGroup(today, 1);
        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> step = daysToDeadline.stream()
                .map(days -> {
                    var steps = mock(ProjectStep.class);
                    when(steps.getDescription()).thenReturn("foo");
                    when(steps.getDaysToDeadline()).thenReturn(days);
                    return steps;
                }).collect(Collectors.toSet()
                );
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(step);
        return result;
    }

    private TaskGroupsRepository getGroupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupsRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties getConfigurationPropertiesReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasksFormTemplate()).thenReturn(result);
        //and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupsRepository {

        private int index = 0;
        private Map<Integer, TaskGroups> map = new HashMap<>();

        private int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroups> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroups> findById(int id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(int projectId) {
            return map.values().stream()
                    .filter(groups -> !groups.isDone())
                    .anyMatch(groups -> groups.getProject() != null & groups.getProject().getId() == projectId);
        }

        @Override
        public TaskGroups save(TaskGroups entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroups.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }
    }
}