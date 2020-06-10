package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.TaskGroups;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadDTO {

    private String description;
    private LocalDateTime deadline;
    private Set<GroupTaskReadDTO> tasks;

    public GroupReadDTO(TaskGroups source){
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(GroupTaskReadDTO::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadDTO> tasks) {
        this.tasks = tasks;
    }
}
