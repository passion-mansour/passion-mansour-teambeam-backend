package passionmansour.teambeam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.enums.TagCategory;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Board> findAllByTagCategory(TagCategory tagCategory, Pageable pageable);
}
