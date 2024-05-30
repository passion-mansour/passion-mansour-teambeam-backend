package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMailAndIsDeletedFalse(String mail);
    Optional<Member> findByMail(String mail);
    Optional<Member> findByMemberIdAndIsDeletedFalse(Long memberId);
}