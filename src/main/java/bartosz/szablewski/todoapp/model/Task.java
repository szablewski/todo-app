package bartosz.szablewski.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Task's description must be not empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroups groups;

    public Task() {
    }

    public Task(@NotBlank(message = "Task's description must be not empty") String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateForm(Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        groups = source.groups;
    }

    TaskGroups getGroups() {
        return groups;
    }

    void setGroups(TaskGroups groups) {
        this.groups = groups;
    }
}
