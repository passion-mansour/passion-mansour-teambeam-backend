package passionmansour.teambeam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import passionmansour.teambeam.service.security.CustomUserDetailsService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final JwtTokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            String username = tokenService.getUsernameFromRefreshToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("username from token: {}", username);
            log.info("userDetails: {}", userDetails);

            log.info("validateRefreshToken {}", tokenService.validateRefreshToken(refreshToken, userDetails));

            if (tokenService.validateRefreshToken(refreshToken, userDetails)) {
                final String newAccessToken = tokenService.generateAccessToken(userDetails);
                log.info("New access token generated: {}", newAccessToken);

                Map<String, String> response = new HashMap<>();
                response.put("message", "New Access Token Issue Successful");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", newAccessToken);
                return ResponseEntity.ok().headers(headers).body(response);
            } else {
                log.warn("Invalid refresh token for user: {}", username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
            }
        } catch (UsernameNotFoundException e) {
            log.error("Error processing refresh token", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error processing request");
        }
    }
}
