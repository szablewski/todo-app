package bartosz.szablewski.todoapp.model.dto;


import bartosz.szablewski.todoapp.model.Task;

public class GroupTaskReadDTO {

    private String description;
    private boolean done;

    public GroupTaskReadDTO(Task source){
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
