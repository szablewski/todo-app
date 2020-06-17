package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throws_IllegalStateException() {
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroups_Id(anyInt())).thenReturn(true);
        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone task");
    }
}