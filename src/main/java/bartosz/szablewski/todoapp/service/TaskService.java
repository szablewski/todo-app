package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private final TaskRepository repository;

    TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> readAllAsync() {
        return CompletableFuture.supplyAsync(repository::findAll);
    }
}
