package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;

import java.util.List;

public interface JoinMemberRepository extends JpaRepository<JoinMember, Long> {
    List<JoinMember> findByMember(Member member);

    JoinMember findByMember_MemberIdAndProject_ProjectId(Long memberId, Long projectId);

    boolean existsByMember_MemberIdAndProject_ProjectId(Long memberId, Long projectId);

    List<JoinMember> findByProject(Project project);
}
