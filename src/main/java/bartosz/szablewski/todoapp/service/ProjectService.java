package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.model.dto.GroupTaskWriteDTO;
import bartosz.szablewski.todoapp.model.dto.GroupWriteDTO;
import bartosz.szablewski.todoapp.model.dto.ProjectWriteDTO;
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
    private TaskGroupService taskGroupService;
    private TaskConfigurationProperties properties;

    public ProjectService(final TaskGroupsRepository repository, final ProjectRepository projectRepository, final TaskGroupService taskGroupService, final TaskConfigurationProperties properties) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.taskGroupService = taskGroupService;
        this.properties = properties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteDTO toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadDTO createGroup(LocalDateTime deadline, int projectId) {
        if (!properties.getTemplate().isAllowMultipleTasksFormTemplate() && repository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteDTO();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteDTO();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroups(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given Id not found"));
    }
}