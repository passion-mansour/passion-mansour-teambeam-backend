package passionmansour.teambeam.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.execption.member.InvalidTokenException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {

    private final RedisTemplate redisTemplate;

    public void storeInvitationToken(String mail, String token) {
        // 토큰을 24시간 저장
        redisTemplate.opsForValue().set("invitation:" + token, mail, 24, TimeUnit.DAYS);
    }

    public String getMailByToken(String token) {
        return (String) redisTemplate.opsForValue().get("invitation:" + token);
    }

    public void deleteInvitationToken(String token) {
        redisTemplate.delete("invitation:" + token);
    }

    // 비밀번호 재설정 토큰
    public void storeResetToken(String token, String mail) {
        redisTemplate.opsForValue().set("reset:" + token, mail, 30, TimeUnit.MINUTES);
    }

    public String getMailByResetToken(String token) {
        String mail = (String) redisTemplate.opsForValue().get("reset:" + token);
        if (mail == null) {
            throw new InvalidTokenException("Token is invalid or expired");
        }
        return mail;
    }

    public void deleteResetToken(String token) {
        redisTemplate.delete("reset:" + token);
    }
}
