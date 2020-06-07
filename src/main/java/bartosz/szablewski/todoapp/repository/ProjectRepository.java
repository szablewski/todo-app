package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(int id);

    Project save(Project entity);

}
