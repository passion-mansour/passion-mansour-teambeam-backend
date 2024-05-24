package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b JOIN b.post p JOIN p.postTags t WHERE t.postTagId IN :postTagId")
    List<Bookmark> findAllByTag(@Param("postTagId") Long postTagId);
}
