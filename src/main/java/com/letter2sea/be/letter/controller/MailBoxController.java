package com.letter2sea.be.letter.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.letter.dto.LetterListResponse;
import com.letter2sea.be.letter.service.LetterService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mailbox/letters")
public class MailBoxController {

    private final JwtProvider jwtProvider;
    private final LetterService letterService;

    @GetMapping
    public List<LetterListResponse> getList(HttpServletRequest request) {
        Long writerId = jwtProvider.decode(request.getHeader("Authorization"));
        return letterService.findList(writerId);
    }
}
