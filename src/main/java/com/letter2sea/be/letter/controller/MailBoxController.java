package com.letter2sea.be.letter.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.letter.dto.response.LetterDetailResponse;
import com.letter2sea.be.letter.dto.response.LetterListResponse;
import com.letter2sea.be.letter.service.LetterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mailbox/letters")
public class MailBoxController {

    private final JwtProvider jwtProvider;
    private final LetterService letterService;

    @GetMapping
    public List<LetterListResponse> getList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long writerId = jwtProvider.decode(authorization);
        return letterService.findList(writerId);
    }

    @GetMapping("/{id}")
    public LetterDetailResponse getDetail(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable Long id) {
        Long writerId = jwtProvider.decode(authorization);
        return letterService.findDetail(id, writerId);
    }

    @GetMapping("/{id}/replies")
    public List<LetterListResponse> getReplyList(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long writerId = jwtProvider.decode(authorization);
        return letterService.findReplyList(id, writerId);
    }
}
