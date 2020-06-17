package bartosz.szablewski.todoapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public static class Template {
        private boolean allowMultipleTasksFormTemplate;

        public boolean isAllowMultipleTasksFormTemplate() {
            return allowMultipleTasksFormTemplate;
        }

        public void setAllowMultipleTasksFormTemplate(boolean allowMultipleTasksFormTemplate) {
            this.allowMultipleTasksFormTemplate = allowMultipleTasksFormTemplate;
        }
    }
}
