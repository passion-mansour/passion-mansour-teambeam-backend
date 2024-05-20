package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.dto.member.request.*;
import passionmansour.teambeam.model.dto.member.response.ErrorResponse;
import passionmansour.teambeam.model.dto.member.response.LoginResponse;
import passionmansour.teambeam.model.dto.member.response.MemberInformationResponse;
import passionmansour.teambeam.model.dto.member.response.RegisterResponse;
import passionmansour.teambeam.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Member Controller", description = "회원 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "사용자 이미 존재",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> saveMember(@Valid @RequestBody RegisterRequest registerRequest) {
        MemberDto member = memberService.saveMember(registerRequest);

        RegisterResponse response = new RegisterResponse("Registration successful", member.getMemberId(), member.getMemberName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 로그인
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "토큰 생성 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) {

        MemberDto member = memberService.login(loginRequest);

        LoginResponse response = new LoginResponse("Login successful", member.getMemberId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", member.getAccessToken());
        headers.add("RefreshToken", member.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(response);
    }

    // 비밀번호 재설정 요청
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 이메일 전송 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/password/send-reset-link")
    public ResponseEntity<?> sendResetLink(@RequestBody UpdateMemberRequest request) {
        memberService.sendPasswordResetLink(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 비밀번호 재설정
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "토큰이 유효하지 않거나 만료됨",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest resetRequest) {

        boolean isReset = memberService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());

        // 비밀번호 재설정 성공
        if (isReset) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // 유효하지 않은 토큰
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 이름 조회
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/member/name")
    public ResponseEntity<?> getMemberName(@RequestHeader("Authorization") String token) {
        MemberDto member = memberService.getMember(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Member information inquiry successful");
        response.put("memberName", member.getMemberName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 정보 조회
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/member")
    public ResponseEntity<?> getMember(@RequestHeader("Authorization") String token) {
        MemberDto member = memberService.getMember(token);

        MemberInformationResponse memberResponse = getMemberResponse(member);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member information inquiry successful");
        response.put("member", memberResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static MemberInformationResponse getMemberResponse(MemberDto member) {
        return MemberInformationResponse.builder()
            .memberId(member.getMemberId())
            .mail(member.getMail())
            .memberName(member.getMemberName())
            .startPage(member.getStartPage())
            .profileImage(member.getProfileImage())
            .notificationCount(member.getNotificationCount())
            .build();
    }

    // 회원 프로필 이미지 조회
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/member/profileImage")
    public ResponseEntity<?> getMemberProfileImage(@RequestHeader("Authorization") String token) {
        MemberDto member = memberService.getMember(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Member information inquiry successful");
        response.put("profileImage", member.getProfileImage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 정보 삭제
    @DeleteMapping("/member")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String token) {
        memberService.deleteMember(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete members successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 비밀번호 수정
    @PatchMapping("/member/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token, @RequestBody UpdatePasswordRequest request) {
        memberService.updatePassword(token, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Change password successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 정보 수정
    @PatchMapping("/member")
    public ResponseEntity<?> updateMember(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        MemberDto member = memberService.updateMember(token, request);

        MemberInformationResponse memberResponse = getMemberResponse(member);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Change member information successfully");
        response.put("updateMember", memberResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 메일 주소 수정 코드 전송
    @PostMapping("/member/mail")
    public ResponseEntity<?> sendUpdateMailCode(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        memberService.sendUpdateMailCode(token, request.getMail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 메일 주소 수정 코드 인증
    @PostMapping("/member/mail-code")
    public ResponseEntity<?> codeAuthentication(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        boolean codeAuthentication = memberService.codeAuthentication(token, request);

        // 인증 성공
        if (codeAuthentication) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Code authentication successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired code");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 메일 수정
    @PatchMapping("/member/mail")
    public ResponseEntity<?> updateMail(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        MemberDto member = memberService.updateMail(token, request);

        MemberInformationResponse memberResponse = getMemberResponse(member);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Change password successfully");
        response.put("updatedMember", memberResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
