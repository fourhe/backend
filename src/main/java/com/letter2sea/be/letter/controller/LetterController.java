package com.letter2sea.be.letter.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.letter.dto.request.LetterCreateRequest;
import com.letter2sea.be.letter.dto.request.ReplyCreateRequest;
import com.letter2sea.be.letter.dto.response.LetterDetailResponse;
import com.letter2sea.be.letter.service.LetterService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/letter")
public class LetterController {

    private final JwtProvider jwtProvider;
    private final LetterService letterService;

    @PostMapping
    public void register(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @Valid @RequestBody LetterCreateRequest letterCreateRequest) {
        Long writerId = jwtProvider.decode(authorization);
        letterService.create(writerId, letterCreateRequest);
    }

    @PostMapping("/{id}/reply")
    public void reply(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestBody ReplyCreateRequest letterReplyRequest) {
        Long writerId = jwtProvider.decode(authorization);
        letterService.reply(id, writerId, letterReplyRequest);
    }

    @GetMapping
    public Map<String, Long> acquire(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        Long randomLetterId = letterService.getRandom(memberId);
        return Collections.singletonMap("id", randomLetterId);
    }

    @GetMapping("/{id}")
    public LetterDetailResponse read(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        return letterService.read(id, memberId);
    }

    //random줍기 관련해서 임시 API
//    @GetMapping("/test")
//    public List<LetterListResponse> randomTest(
//        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
//        Long memberId = jwtProvider.decode(authorization);
//        return letterService.randomTest(memberId);
//    }
}
