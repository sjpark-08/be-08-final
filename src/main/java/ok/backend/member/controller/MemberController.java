package ok.backend.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ok.backend.member.domain.entity.Member;
import ok.backend.member.dto.MemberLoginRequestDto;
import ok.backend.member.dto.MemberRegisterRequestDto;
import ok.backend.member.dto.MemberResponseDto;
import ok.backend.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "User", description = "회원 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        MemberResponseDto memberResponseDto = memberService.registerMember(memberRegisterRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberResponseDto);
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberService.findMemberByEmailAndPassword(memberLoginRequestDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, memberService.createToken(member).toString())
                .body(new MemberResponseDto(member));
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        memberService.logout(request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 수정 API")
    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestParam String nickname, @RequestParam String content,
                                                          @RequestParam MultipartFile file) throws IOException {

        return ResponseEntity.ok(memberService.updateMember(nickname, content, file));
    }

    @Operation(summary = "회원 탈퇴 API")
    @PutMapping("/delete")
    public ResponseEntity<String> deleteMember(HttpServletRequest request){
        memberService.deleteMember(request);

        return ResponseEntity.ok().build();
    }


}
