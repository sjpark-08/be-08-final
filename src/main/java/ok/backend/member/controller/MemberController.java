package ok.backend.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ok.backend.member.domain.entity.Member;
import ok.backend.member.dto.*;
import ok.backend.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + memberService.createToken(member))
                .body(memberService.convertToDto(member));
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        memberService.logout();

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 수정 API")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberResponseDto> updateMember(@RequestPart(value = "user") MemberUpdateRequestDto memberUpdateRequestDto,
                                                          @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                          @RequestPart(value = "background", required = false) MultipartFile background) throws IOException {

        return ResponseEntity.ok(memberService.updateMember(memberUpdateRequestDto, profile, background));
    }

    @Operation(summary = "회원 탈퇴 API")
    @PutMapping("/delete")
    public ResponseEntity<String> deleteMember(@RequestBody MemberLoginRequestDto memberLoginRequestDto){
        memberService.deleteMember(memberLoginRequestDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 회원들의 프로필 정보를 반환하는 API")
    @GetMapping("/list")
    public ResponseEntity<List<MemberProfileResponseDto>> getMemberProfiles(@RequestParam List<Long> memberIdList) throws MalformedURLException {
        return ResponseEntity.ok(memberService.getMemberProfiles(memberIdList));
    }

    @Operation(summary = "회원 계정 패스워드 변경 API")
    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestParam String previous, @RequestParam String current){
        memberService.updatePasswordByPrevious(previous, current);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 중복 검사 API")
    @PostMapping("/check/email")
    public ResponseEntity<String> checkEmailExist(@RequestParam String email){
        memberService.checkEmailExist(email);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임 중복 검사 API")
    @PostMapping("/check/nickname")
    public ResponseEntity<String> checkNickNameExist(@RequestParam String nickName){
        memberService.checkNickNameExist(nickName);

        return ResponseEntity.ok().build();
    }

}
