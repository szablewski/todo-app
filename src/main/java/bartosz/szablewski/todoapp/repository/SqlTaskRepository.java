package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer group_id);
}
