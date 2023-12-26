package com.letter2sea.be.member.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.member.dto.MemberUpdateRequest;
import com.letter2sea.be.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @PutMapping
    public void update(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @Valid @RequestBody MemberUpdateRequest request) {
        Long memberId = jwtProvider.decode(authorization);
        memberService.update(memberId, request);
    }
}
