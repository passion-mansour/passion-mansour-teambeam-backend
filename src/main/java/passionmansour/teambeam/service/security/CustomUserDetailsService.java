package passionmansour.teambeam.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMailAndIsDeletedFalse(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with memberName: " + username));

        return User.builder()
            .username(member.getMemberName())
            .password(member.getPassword())
            .roles("USER") // 기본 역할
            .build();
    }

}