package com.letter2sea.be.letter.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.letter.dto.response.LetterDetailResponse;
import com.letter2sea.be.letter.dto.response.LetterPaginatedResponse;
import com.letter2sea.be.letter.dto.response.ReplyListResponse;
import com.letter2sea.be.letter.service.LetterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public LetterPaginatedResponse getList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, Pageable pageable) {
        Long writerId = jwtProvider.decode(authorization);
        return letterService.findAll(pageable, writerId);
    }

    @GetMapping("/{id}")
    public LetterDetailResponse getDetail(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable Long id) {
        Long writerId = jwtProvider.decode(authorization);
        return letterService.findDetail(id, writerId);
    }

    @GetMapping("/{id}/replies")
    public List<ReplyListResponse> getReplyList(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        return letterService.findReplyList(id, memberId);
    }

    @GetMapping("/{id}/replies/{replyId}")
    public LetterDetailResponse getReplyDetail(@PathVariable Long id, @PathVariable Long replyId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        return letterService.findReplyDetail(id, replyId, memberId);
    }

    @PostMapping("/{id}")
    public void delete(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        letterService.delete(id, memberId);
    }

    @PostMapping("/{replyId}/thanks")
    public void thanks(@PathVariable Long replyId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        letterService.thanks(replyId, memberId);
    }
}
