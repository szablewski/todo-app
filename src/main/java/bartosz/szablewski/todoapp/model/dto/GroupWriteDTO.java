package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Project;
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

    public TaskGroups toGroup(Project project) {
        var result = new TaskGroups();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        result.setProject(project);
        return result;
    }
}
