package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("SELECT t FROM PostTag t WHERE t.tag.tagId = :tagId")
    PostTag findByTagId(@Param("tagId") Long tagId);
}
