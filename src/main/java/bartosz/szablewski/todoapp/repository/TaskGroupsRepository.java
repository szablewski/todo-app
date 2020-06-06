package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.TaskGroups;

import java.util.List;
import java.util.Optional;

public interface TaskGroupsRepository {
    List<TaskGroups> findAll();

    Optional<TaskGroups> findById();

    TaskGroups save(TaskGroups entity);

}
