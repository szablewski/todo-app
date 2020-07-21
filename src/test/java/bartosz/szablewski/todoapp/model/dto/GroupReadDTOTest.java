package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.TaskGroups;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GroupReadDTOTest {

    @Test
    @DisplayName("should create null deadline for group when no task deadline")
    void constructor_noDeadlines_createsNullDeadline() {
        // given
        var source = new TaskGroups();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        //when
        var result = new GroupReadDTO(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}