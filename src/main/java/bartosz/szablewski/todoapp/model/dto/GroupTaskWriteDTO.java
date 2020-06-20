package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.TaskGroups;

import java.time.LocalDateTime;

public class GroupTaskWriteDTO {

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

    public Task toTask(TaskGroups groups){
        return new Task(description, deadline, groups);

    }
}
