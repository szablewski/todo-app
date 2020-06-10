package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.TaskGroups;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteDTO {

    private String description;
    private Set<GroupTaskWriteDTO> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteDTO> tasks) {
        this.tasks = tasks;
    }

    public TaskGroups toGroup() {
        var result = new TaskGroups();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(GroupTaskWriteDTO::toTask)
                        .collect(Collectors.toSet())
        );
        return result;
    }
}
