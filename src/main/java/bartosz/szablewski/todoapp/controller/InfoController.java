package bartosz.szablewski.todoapp.controller;

import bartosz.szablewski.todoapp.TaskConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class InfoController {

    private TaskConfigurationProperties myProp;

    public InfoController(final TaskConfigurationProperties myProp) {
        this.myProp = myProp;
    }

    @GetMapping("/info/prop")
    boolean prop() {
        return myProp.isAllowMultipleTasksFormTemplate();
    }
}
