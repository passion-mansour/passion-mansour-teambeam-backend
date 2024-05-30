package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Post;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.member.memberId IN :memberId")
    List<Bookmark> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT b FROM Bookmark b JOIN b.post p JOIN p.postTags t WHERE t.postTagId IN :postTagId AND b.member.memberId IN :memberId")
    List<Bookmark> findAllByTag(@Param("postTagId") Long postTagId, @Param("memberId") Long memberId);

    @Query(value = "SELECT b.* " +
            "FROM bookmark b " +
            "JOIN post p ON b.post_id = p.post_id " +
            "JOIN post_tag pt ON p.post_id = pt.post_id " +
            "WHERE b.member_id = :memberId AND pt.tag_id IN (:tagIds) " +
            "GROUP BY b.bookmark_id, p.post_id " +
            "HAVING COUNT(DISTINCT pt.tag_Id) = :tagCount",
            nativeQuery = true)
    List<Bookmark> findAllByTagIds(@Param("memberId") Long memberId, @Param("tagIds") List<Long> tagIds, @Param("tagCount") int tagCount);

}
