package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.Task;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.model.dto.GroupTaskWriteDTO;
import bartosz.szablewski.todoapp.model.dto.GroupWriteDTO;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import bartosz.szablewski.todoapp.repository.TaskRepository;
import bartosz.szablewski.todoapp.service.TaskGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
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

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteDTO());
        return "groups";
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadDTO> createGroup(@RequestBody @Valid GroupWriteDTO toCreate) {
        GroupReadDTO result = service.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addProject(@ModelAttribute("group") @Valid GroupWriteDTO current, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        service.save(current);
        model.addAttribute("group", new GroupWriteDTO());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupe!");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addSteps(@ModelAttribute("group") GroupWriteDTO current) {
        current.getTasks().add(new GroupTaskWriteDTO());
        return "groups";
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadDTO>> readAllGroups() {
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.findAllByGroups_Id(id));
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ModelAttribute("groups")
    List<GroupReadDTO> getGroups() {
        return service.readAll();
    }
}
