package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT i FROM Board i WHERE i.project.projectId = :projectId")
    List<Board> getAllBoardByProjectId(@Param("projectId") Long projectId);
}