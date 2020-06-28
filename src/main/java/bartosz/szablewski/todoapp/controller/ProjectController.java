package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.model.dto.ProjectWriteDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
    @GetMapping
    public String projectForm(Model model) {
        model.addAttribute("project", new ProjectWriteDTO());
        return "projects";
    }
}
