package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Post;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.member.memberId = :memberId")
    List<Bookmark> findAllByMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT DISTINCT b.* " +
            "FROM bookmark b " +
            "JOIN post p ON b.post_id = p.post_id " +
            "JOIN post_tag pt ON p.post_id = pt.post_id " +
            "WHERE b.member_id = :memberId " +
            "AND b.is_deleted = false " +
            "AND pt.is_deleted = false " +
            "AND pt.tag_id IN (:tagIds) ",
            nativeQuery = true)
    List<Bookmark> findAllByTagIds(@Param("memberId") Long memberId, @Param("tagIds") List<Long> tagIds);

    @Query(value = "SELECT b.* FROM bookmark b WHERE b.member_id = (:memberId) AND b.post_id = (:postId) AND b.is_deleted = true", nativeQuery = true)
    Bookmark findByMemberAndPostAndIsDeleted(@Param("memberId") Long memberId, @Param("postId") Long postId);
}
