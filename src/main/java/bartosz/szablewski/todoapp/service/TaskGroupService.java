package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.TaskGroups;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.model.dto.GroupWriteDTO;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {

    private final TaskGroupsRepository repository;
    private final TaskRepository taskRepository;

    TaskGroupService(final TaskGroupsRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadDTO createGroups(GroupWriteDTO source) {
        return createGroups(source, null);
    }

    GroupReadDTO createGroups(GroupWriteDTO source, Project project) {
        TaskGroups result = repository.save(source.toGroup(project));
        return new GroupReadDTO(result);
    }

    public List<GroupReadDTO> readAll() {
        return repository.findAll().stream()
                .map(GroupReadDTO::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroups_Id(groupId)) {
            throw new IllegalStateException("Group has undone task. Done all the tasks first");
        }
        TaskGroups result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given ID not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
