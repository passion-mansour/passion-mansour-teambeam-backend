package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.Project;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT DISTINCT p.* " +
            "FROM post p " +
            "JOIN post_tag pt ON p.post_id = pt.post_id " +
            "WHERE p.is_deleted = false " +
            "AND pt.is_deleted = false " +
            "AND pt.tag_id IN (:tagIds)",
            nativeQuery = true)
    List<Post> findAllByTagIds(@Param("tagIds") List<Long> tagIds);

    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId")
    List<Post> findAllByBoardId(@Param("boardId") Long boardId);

    List<Post> findAllByNoticeIsTrueAndProject(Project project);
}
