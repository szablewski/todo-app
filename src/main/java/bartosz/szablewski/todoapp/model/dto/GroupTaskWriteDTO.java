package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.TaskGroups;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GroupTaskWriteDTO {

    @NotBlank(message = "Task's description must be not empty")
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(TaskGroups groups) {
        return new Task(description, deadline, groups);

    }
}
