package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.memberId IN :memberId")
    List<Bookmark> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT b FROM Bookmark b JOIN b.post p JOIN p.postTags t WHERE t.postTagId b.IN :postTagId AND b.memberId IN :memberId")
    List<Bookmark> findAllByTag(@Param("memberId") Long memberId, @Param("postTagId") Long postTagId);
}
