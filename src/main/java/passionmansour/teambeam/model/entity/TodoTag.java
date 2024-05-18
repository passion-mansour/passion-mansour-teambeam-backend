package passionmansour.teambeam.model.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class TodoTag {
    @ManyToOne
    @JoinColumn(name = "bottomTodoId")
    private BottomTodo bottomTodo;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
