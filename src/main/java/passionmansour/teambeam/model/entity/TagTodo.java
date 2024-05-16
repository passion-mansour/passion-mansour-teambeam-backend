package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class TagTodo {
    @Id
    @GeneratedValue
    @Column(name="tagTodoId")
    private Long tagTodoId;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "bottomTodoId")
    private BottomTodo bottomTodo;
}
