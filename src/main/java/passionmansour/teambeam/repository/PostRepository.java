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
    @Query("SELECT p FROM Post p WHERE p.postTitle = :postTitle")
    Optional<PostResponse> findByTitle(String title);

    @Query("SELECT p FROM Post p JOIN p.postTags t WHERE t.postTagId IN :postTagId")
    List<PostResponse> findAllByTag(@Param("postTagId") Long tagId);

    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId")
    PostListResponse findAllByBoardId(@Param("boardId") Long boardId);
}
