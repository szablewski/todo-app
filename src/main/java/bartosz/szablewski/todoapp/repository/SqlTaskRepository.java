package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    boolean existsByDoneIsFalseAndGroups_Id(int groupId);

    @Override
    List<Task> findAllByGroups_Id (Integer groupId);
}
