package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.TaskGroups;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.repository.ProjectRepository;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private TaskGroupsRepository repository;
    private ProjectRepository projectRepository;
    private TaskConfigurationProperties properties;

    public ProjectService(final TaskGroupsRepository repository, final ProjectRepository projectRepository, final TaskConfigurationProperties properties) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.properties = properties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadDTO createGroup(LocalDateTime deadline, int projectId) {
        if (!properties.getTemplate().isAllowMultipleTasksFormTemplate() && repository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroups result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroups();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> new Task(
                                            projectStep.getDescription(),
                                            deadline.plusDays(projectStep.getDaysToDeadline()))
                                    ).collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return repository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given Id not found"));
        return new GroupReadDTO(result);
    }
}