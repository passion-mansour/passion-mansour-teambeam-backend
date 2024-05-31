package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.PostComment;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("SELECT i FROM PostComment i JOIN i.post p WHERE p.postId = :postId")
    List<PostComment> getAllByPostId(@Param("postId") Long postId);
}
