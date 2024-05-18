package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
