package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.TaskGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWriteDTO {

    @NotBlank(message = "Task group's description must be not empty")
    private String description;
    @Valid
    private List<GroupTaskWriteDTO> tasks = new ArrayList<>();

    public GroupWriteDTO() {
        tasks.add(new GroupTaskWriteDTO());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GroupTaskWriteDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<GroupTaskWriteDTO> tasks) {
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
