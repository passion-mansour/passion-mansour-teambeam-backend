package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Member;

import java.util.List;

public interface JoinMemberRepository extends JpaRepository<JoinMember, Long> {
    List<JoinMember> findByMember(Member member);
}
