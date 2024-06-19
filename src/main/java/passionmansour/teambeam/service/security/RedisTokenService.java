package passionmansour.teambeam.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.execption.member.InvalidTokenException;
import passionmansour.teambeam.model.dto.project.InvitationTokenDto;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void storeInvitationToken(InvitationTokenDto invitationTokenDto, String token) {
        try {
            // InvitationTokenDto 객체를 JSON 문자열로 변환
            String jsonString = objectMapper.writeValueAsString(invitationTokenDto);
            // JSON 문자열을 Redis에 저장
            redisTemplate.opsForValue().set("invitation:" + token, jsonString, 1, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store invitation token", e);
        }
    }

    // 메일 정보를 기준으로 저장된 토큰을 검색하는 메서드
    private String findInvitationTokenByMail(String mail) {
        // invitation:* 키를 검색 (주의: 실제 사용시 키 개수에 주의)
        Set<String> keys = redisTemplate.keys("invitation:*");
        if (keys != null) {
            for (String key : keys) {
                try {
                    String jsonString = (String) redisTemplate.opsForValue().get(key);
                    if (jsonString != null) {
                        InvitationTokenDto storedDto = objectMapper.readValue(jsonString, InvitationTokenDto.class);
                        if (storedDto != null && mail.equals(storedDto.getMail())) {
                            // 토큰은 "invitation:" 이후의 문자열이므로 이를 추출하여 반환
                            return key.substring("invitation:".length());
                        }
                    }
                } catch (Exception e) {
                    throw new NoSuchElementException("Token not found whit mail: " + mail);
                }
            }
        }
        return null;
    }

    public InvitationTokenDto geObjectByInvitationToken(String token) {
        try {
            String jsonString = (String) redisTemplate.opsForValue().get("invitation:" + token);
            if (jsonString != null) {
                return objectMapper.readValue(jsonString, InvitationTokenDto.class);
            } else {
                throw new SignatureException("Invalid token or token expired");
            }
        } catch (Exception e) {
            throw new SignatureException("Failed to parse token: " + e.getMessage());
        }
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
