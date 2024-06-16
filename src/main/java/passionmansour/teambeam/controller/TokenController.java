package passionmansour.teambeam.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import passionmansour.teambeam.service.security.CustomUserDetailsService;
import passionmansour.teambeam.service.security.JwtTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final JwtTokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            String username = tokenService.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenService.validateToken(refreshToken, userDetails)) {
                String newAccessToken = tokenService.generateAccessToken(userDetails);
                return ResponseEntity.ok(new TokenResponse(newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Could not refresh token");
        }
    }

    @Data
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
    }
}
