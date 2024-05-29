package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p.* " +
            "FROM Post p " +
            "JOIN Post_Tag pt ON p.post_Id = pt.post_Id " +
            "WHERE pt.tag_Id IN (:tagIds) " +
            "GROUP BY p.post_Id " +
            "HAVING COUNT(DISTINCT pt.tag_Id) = :tagCount",
            nativeQuery = true)
    List<Post> findAllByTagIds(@Param("tagIds") List<Long> tagIds, @Param("tagCount") int tagCount);

    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId")
    List<Post> findAllByBoardId(@Param("boardId") Long boardId);
}
