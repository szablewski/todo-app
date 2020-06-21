package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    boolean existsById(int id);

    boolean existsByDoneIsFalseAndGroups_Id(int groupId);

    List<Task> findByDone(boolean done);

    Task save(Task entity);

    List<Task> findAllByGroups_Id (Integer groupId);
}
