package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoTagId;

    @ManyToOne
    @JoinColumn(name = "bottomTodoId")
    private BottomTodo todo;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
