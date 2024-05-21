package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
public class TodoTag {

    @Id
    @GeneratedValue
    private Long todoTagId;

    @ManyToOne
    @JoinColumn(name = "bottomTodoId")
    private BottomTodo bottomTodo;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
