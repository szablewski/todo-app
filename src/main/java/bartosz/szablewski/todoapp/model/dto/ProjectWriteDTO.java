package bartosz.szablewski.todoapp.model.dto;

import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteDTO {
    @NotBlank(message = "Project's description must be not empty")
    private String description;
    @Valid
    private List<ProjectStep> steps;

    public ProjectWriteDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
