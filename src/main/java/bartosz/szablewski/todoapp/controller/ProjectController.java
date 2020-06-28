package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.Project;
import bartosz.szablewski.todoapp.model.ProjectStep;
import bartosz.szablewski.todoapp.model.dto.ProjectWriteDTO;
import bartosz.szablewski.todoapp.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    String addProject(@ModelAttribute("project") ProjectWriteDTO current, Model model){
        projectService.save(current);
        model.addAttribute("project", new ProjectWriteDTO());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep")
     String addSteps(@ModelAttribute("project") ProjectWriteDTO current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return projectService.readAll();
    }
}
