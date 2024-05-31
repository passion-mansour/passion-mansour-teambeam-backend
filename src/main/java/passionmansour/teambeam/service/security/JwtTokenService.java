package passionmansour.teambeam.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.repository.MemberRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    // 서명 키 생성
    private SecretKey getSigningKey(String secret) {
        log.debug("Decoding secret key: {}", secret);
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    private String createToken(Map<String, Object> claims, String subject, long expiration, SecretKey signingKey) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(signingKey, SignatureAlgorithm.HS512)
            .compact();
    }

    // 액세스 토큰 생성
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), expiration, getSigningKey(secret));
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpiration, getSigningKey(refreshSecret));
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject, getSigningKey(secret));
    }

    // 토큰에서 유효 기간 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration, getSigningKey(secret));
    }

    // 특정 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, SecretKey signingKey) {
        final Claims claims = getAllClaimsFromToken(token, signingKey);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출
    private Claims getAllClaimsFromToken(String token, SecretKey signingKey) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // 토큰 유효 기간 확인
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 토큰 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 초대 토큰 생성
    public String generateInvitationToken(String mail, Long projectId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("projectId", projectId);
        return createToken(claims, mail, 86400, getSigningKey(secret));
    }

    // 초대 토큰 검증 및 파싱
    public Claims decodeToken(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey(secret))
            .parseClaimsJws(token)
            .getBody();
    }

    // 초대 토큰에서 프로젝트 ID 추출
    public Long getProjectIdFromToken(String token) {
        Claims claims = decodeToken(token);
        return claims.get("projectId", Long.class);
    }

    // 토큰에서 추출한 메일로 멤버 조회
    public Member getMemberByToken(String token) {
        // 토큰에서 회원 메일 확인
        String usernameFromToken = getUsernameFromToken(token);

        // 해당 회원 정보 조회
        return memberRepository.findByMailAndIsDeletedFalse(usernameFromToken)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with memberName: " + usernameFromToken));
    }
}