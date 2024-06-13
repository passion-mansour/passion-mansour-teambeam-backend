package passionmansour.teambeam.controller.member;

import io.swagger.v3.oas.annotations.Operation;
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
import passionmansour.teambeam.model.dto.member.response.*;
import passionmansour.teambeam.service.MemberService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Member Controller", description = "회원 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 위해 필수 정보를 입력합니다.")
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
        MemberDto member = null;
        try {
            member = memberService.saveMember(registerRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RegisterResponse response = new RegisterResponse("Registration successful", member.getMemberId(), member.getMemberName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "회원가입 메일 인증", description = "사용자가 회원가입을 위한 메일 주소 인증 코드 메일 발송을 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메일 발송 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register-request")
    public ResponseEntity<?> sendRegisterCode(@RequestBody UpdateMemberRequest request) {
        String code = memberService.sendRegisterCode(request.getMail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        response.put("code", code);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 로그인
    @Operation(summary = "로그인", description = "사용자가 로그인을 위해 필수 정보를 입력합니다.")
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
    @Operation(summary = "비밀번호 재설정", description = "사용자가 비밀번호 재설정을 위해 메일 주소를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 메일 전송 성공",
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
        String link = memberService.sendPasswordResetLink(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        response.put("url", link);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 비밀번호 재설정
    @Operation(summary = "비밀번호 재설정", description = "사용자가 비밀번호 재설정을 위한 인증 코드와 새로운 비밀번호를 입력합니다.")
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
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        boolean isReset = memberService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());

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
    @Operation(summary = "회원 이름 조회", description = "사용자의 회원 정보 중 이름만 조회합니다.")
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
    @Operation(summary = "회원 정보 조회", description = "사용자의 모든 회원 정보를 조회합니다.")
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
    @Operation(summary = "회원 프로필 이미지 조회", description = "사용자의 회원 정보 중 프로필 이미지만 조회합니다.")
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
    @GetMapping("/member/profileImage/{memberId}")
    public ResponseEntity<?> getMemberProfileImage(@RequestHeader("Authorization") String token,
                                                   @PathVariable("memberId") Long memberId) {
        MemberDto member = memberService.getMemberById(token, memberId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member information inquiry successful");
        response.put("memberId", member.getMemberId());
        response.put("profileImage", member.getProfileImage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 이미지 모두 조회
    @GetMapping("/member/profileImages")
    public ResponseEntity<?> getProfileImages(@RequestHeader("Authorization") String token) throws IOException {
        List<ProfileImageResponse> profileImages = memberService.getProfileImages();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "ProfileImage inquiry successful");
        response.put("profileImages", profileImages);

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
    @Operation(summary = "비밀번호 수정", description = "사용자가 비밀번호 수정을 위해 필수 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 비밀번호",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/member/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token, @RequestBody UpdatePasswordRequest request) {
        memberService.updatePassword(token, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully updated password");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 수정", description = "사용자가 회원 정보 수정을 위해 수정할 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/member")
    public ResponseEntity<?> updateMember(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        MemberDto member = memberService.updateMember(token, request);

        MemberInformationResponse memberResponse = getMemberResponse(member);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully updated member details");
        response.put("updatedMember", memberResponse);

        if (member.getAccessToken() != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", member.getAccessToken());
            headers.add("RefreshToken", member.getRefreshToken());
            return ResponseEntity.ok().headers(headers).body(response);

        }
        return ResponseEntity.ok().body(response);
    }

    // 메일 주소 수정 코드 전송
    @Operation(summary = "회원 정보(메일 주소) 수정", description = "사용자가 메일 주소 수정을 위해 수정할 메일 주소를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 메일 전송 성공",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 메일",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/member/mail")
    public ResponseEntity<?> sendUpdateMailCode(@RequestHeader("Authorization") String token, @RequestBody UpdateMemberRequest request) {
        String code = memberService.sendUpdateMailCode(token, request.getMail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        response.put("code", code);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
