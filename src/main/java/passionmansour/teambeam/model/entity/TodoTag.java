package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE todo_tag SET is_deleted = true WHERE todo_tag_id = ?")
@SQLRestriction("is_deleted = false")
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

    private boolean is_deleted = false;
}
