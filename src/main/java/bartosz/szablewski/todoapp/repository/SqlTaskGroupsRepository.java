package bartosz.szablewski.todoapp.repository;

import bartosz.szablewski.todoapp.model.TaskGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupsRepository extends TaskGroupsRepository, JpaRepository<TaskGroups, Integer> {

    @Override
    @Query("from TaskGroups g join fetch g.tasks")
    List<TaskGroups> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(int projectId);
}
