package bartosz.szablewski.todoapp;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.SequenceGenerator;
import javax.sql.DataSource;
import java.util.*;

@Configuration
class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    @SequenceGenerator(name = "hibernate_sequence", schema = "hibernate_sequence")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile("integration")
    TaskRepository taskRepo() {
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById(int id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroups_Id(int groupId) {
                return false;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public Task save(Task entity) {
                int key = tasks.size() + 1;
                try{
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,key);
                }catch (NoSuchFieldException | IllegalAccessException e){
                    throw new RuntimeException(e);
                }
                tasks.put(key,entity);
                return tasks.get(key);
            }

            @Override
            public List<Task> findAllByGroups_Id(Integer groupId) {
                return List.of();
            }
        };
    }
}