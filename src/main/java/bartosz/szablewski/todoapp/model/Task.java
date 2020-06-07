package bartosz.szablewski.todoapp.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
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
