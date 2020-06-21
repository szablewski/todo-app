package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.model.dto.GroupWriteDTO;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import bartosz.szablewski.todoapp.service.TaskGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService service;
    private final TaskGroupsRepository groupsRepository;
    private final TaskRepository repository;

    TaskGroupController(final TaskGroupService service, TaskGroupsRepository groupsRepository, final TaskRepository repository) {
        this.service = service;
        this.groupsRepository = groupsRepository;
        this.repository = repository;
    }

    @PostMapping
    ResponseEntity<GroupReadDTO> createGroup(@RequestBody @Valid GroupWriteDTO toCreate) {
        GroupReadDTO result = service.createGroups(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadDTO>> readAllGroups() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.findAllByGroups_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handlerIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
