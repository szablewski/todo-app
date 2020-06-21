package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
class InfoController {

    private TaskConfigurationProperties myProp;

    public InfoController(final TaskConfigurationProperties myProp) {
        this.myProp = myProp;
    }

    @GetMapping("/prop")
    boolean prop() {
        return myProp.getTemplate().isAllowMultipleTasksFormTemplate();
    }
}
