package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
