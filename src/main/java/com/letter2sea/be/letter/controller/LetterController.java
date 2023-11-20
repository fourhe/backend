package com.letter2sea.be.letter.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.letter.dto.LetterCreateRequest;
import com.letter2sea.be.letter.service.LetterService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/letter")
public class LetterController {

    private final JwtProvider jwtProvider;
    private final LetterService letterService;

    @PostMapping
    public void register(HttpServletRequest request,
        @RequestBody LetterCreateRequest letterCreateRequest) {
        Long writerId = jwtProvider.decode(request.getHeader("Authorization"));
        letterService.create(writerId, letterCreateRequest);
    }

    @GetMapping
    public Map<String, Long> acquire(HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        Long randomLetterId = letterService.getRandom(memberId);
        return Collections.singletonMap("id", randomLetterId);
    }
}
