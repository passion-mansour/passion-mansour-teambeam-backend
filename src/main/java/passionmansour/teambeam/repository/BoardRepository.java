package passionmansour.teambeam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
