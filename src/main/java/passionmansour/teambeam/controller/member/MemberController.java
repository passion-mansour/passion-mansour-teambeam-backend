package passionmansour.teambeam.controller.member;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.member.LoginDto;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.dto.member.response.ErrorResponseDto;
import passionmansour.teambeam.model.dto.member.response.LoginResponseDto;
import passionmansour.teambeam.model.dto.member.response.RegisterResponseDto;
import passionmansour.teambeam.model.dto.member.RegisterDto;
import passionmansour.teambeam.service.MemberService;

@Tag(name = "Member Controller", description = "회원 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "사용자 이미 존재",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> saveMember(@Valid @RequestBody RegisterDto registerDto) {
        MemberDto member = memberService.saveMember(registerDto);

        RegisterResponseDto response = new RegisterResponseDto("Registration successful", member.getMemberId(), member.getMemberName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "토큰 생성 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> createAuthenticationToken(@Valid @RequestBody LoginDto loginDto) {

        MemberDto member = memberService.login(loginDto);

        LoginResponseDto response = new LoginResponseDto("Login successful", member.getAccessToken(), member.getRefreshToken(), member.getMemberId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable(name = "id") Long id) {
        memberService.deleteMember(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
