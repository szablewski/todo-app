package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import bartosz.szablewski.todoapp.repository.ProjectRepository;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

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
        TaskGroupsRepository mockGroupRepository = getGroupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mockConfig = getConfigurationPropertiesReturning(true);
        //system under test
        var toTest = new ProjectService(mockGroupRepository, mockProjectRepository, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");
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
}