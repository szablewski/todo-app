package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.ProjectStep;
import bartosz.szablewski.todoapp.model.dto.ProjectWriteDTO;
import bartosz.szablewski.todoapp.service.ProjectService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService projectService;

    ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String projectForm(Model model) {
        model.addAttribute("project", new ProjectWriteDTO());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteDTO current, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        projectService.save(current);
        model.addAttribute("project", new ProjectWriteDTO());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addSteps(@ModelAttribute("project") ProjectWriteDTO current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteDTO current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectService.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupę!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.readAll();
    }
}
