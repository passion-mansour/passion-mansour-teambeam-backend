package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passionmansour.teambeam.model.entity.TodoTag;

import java.util.List;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
    @Query("SELECT t FROM TodoTag t WHERE t.tag.tagId = :tagId")
    TodoTag findByTagId(@Param("tagId") Long tagId);
}
