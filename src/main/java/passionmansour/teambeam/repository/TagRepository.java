package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Tag;
import passionmansour.teambeam.model.enums.TagCategory;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t WHERE t.project.projectId = :projectId")
    List<Tag> findByProject_ProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM Tag t WHERE t.project.projectId = :projectId AND t.tagCategory = :tagCategory")
    List<Tag> findByTagCategory(@Param("projectId") Long projectId, @Param("tagCategory") TagCategory tagCategory);
}
