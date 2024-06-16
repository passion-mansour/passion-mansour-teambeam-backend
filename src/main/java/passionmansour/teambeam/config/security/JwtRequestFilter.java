package passionmansour.teambeam.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import passionmansour.teambeam.service.security.CustomUserDetailsService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        final String refreshTokenHeader = request.getHeader("RefreshToken");

        String username = null;
        String jwtToken = null;

        try {
            if (requestTokenHeader != null) {
                jwtToken = requestTokenHeader;
                try {
                    username = jwtTokenService.getUsernameFromToken(jwtToken);
                } catch (ExpiredJwtException e) {
                    username = handleExpiredToken(request, response, refreshTokenHeader);
                    if (username != null) {
                        // 새로운 토큰으로 헤더를 업데이트하고 요청을 재시도
                        String newToken = response.getHeader("Authorization");
                        if (newToken != null) {
                            request.setAttribute("Authorization", newToken);
                            // 새 토큰을 로그에 기록
                            log.info("New token generated: {}", newToken);
                        }
                        // chain.doFilter 호출을 통해 다시 필터링을 진행
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    logger.error("Unable to get JWT Token", e);
                }
            } else {
                logger.warn("JWT Token does not exist");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(username, jwtToken, request);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception occurred while processing JWT Token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String handleExpiredToken(HttpServletRequest request, HttpServletResponse response, String refreshTokenHeader) throws SignatureException {
        if (refreshTokenHeader != null) {
            try {
                String username = jwtTokenService.getUsernameFromToken(refreshTokenHeader);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenService.validateToken(refreshTokenHeader, userDetails)) {
                    String newToken = jwtTokenService.generateAccessToken(userDetails);
                    response.setHeader("Authorization", newToken);
                    log.info("New token set in response header: {}", newToken);  // 로그에 기록
                    return username;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get Refresh Token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("Refresh Token has expired", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            log.warn("Refresh Token is missing or invalid");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return null;
    }

    private void authenticateUser(String username, String jwtToken, HttpServletRequest request) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (jwtTokenService.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
