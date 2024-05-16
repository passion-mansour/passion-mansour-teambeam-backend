package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Board {
    @Id
    @GeneratedValue
    @Column(name="boardId")
    private Long boardId;

    private String boardName;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}
