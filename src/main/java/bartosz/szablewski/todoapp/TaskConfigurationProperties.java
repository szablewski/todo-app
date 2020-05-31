package bartosz.szablewski.todoapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {

    private boolean allowMultipleTasksFormTemplate;

    public boolean isAllowMultipleTasksFormTemplate() {
        return allowMultipleTasksFormTemplate;
    }

    public void setAllowMultipleTasksFormTemplate(boolean allowMultipleTasksFormTemplate) {
        this.allowMultipleTasksFormTemplate = allowMultipleTasksFormTemplate;
    }
}
