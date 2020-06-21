package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import bartosz.szablewski.todoapp.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);
    private final TaskRepository taskRepository;
    private final TaskService service;

    TaskController(final TaskRepository taskRepository, TaskService service) {
        this.taskRepository = taskRepository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = taskRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return service.readAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable("id") int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                taskRepository.findByDone(state)
        );
    }

    @Transactional
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(id)) {
            logger.warn("ERROR exist task by ID: " + id);
            return ResponseEntity.notFound().build();
        }

        taskRepository.findById(id)
                .ifPresent(task -> task.updateForm(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            logger.warn("ERROR exist task by ID: " + id);
            return ResponseEntity.notFound().build();
        }

        taskRepository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }


}
