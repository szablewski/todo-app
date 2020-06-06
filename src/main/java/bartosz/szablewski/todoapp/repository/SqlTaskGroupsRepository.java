package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.TaskGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskGroupsRepository extends TaskGroupsRepository, JpaRepository<TaskGroups, Integer> {
}
