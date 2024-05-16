package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import passionmansour.teambeam.model.enums.TagCategory;

@Entity
@Table
public class Tag {
    @Id
    @GeneratedValue
    @Column(name="tagId")
    private Long tagId;

    private String tagName;
    private TagCategory tagCategory;
}
