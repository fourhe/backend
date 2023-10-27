package com.letter2sea.be.auth.controller;

import com.letter2sea.be.auth.oauth.OAuthProperties;
import com.letter2sea.be.auth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OAuthProperties oauthProperties;

    private final OAuthService authService;

    @GetMapping("/login/form")
    public void redirectLoginForm(HttpServletResponse response) throws IOException {
        response.sendRedirect(oauthProperties.getLoginFormUrl());
    }

    @PostMapping("/login")
    public void login(@RequestBody Map<String, String> map) {
        String code = map.get("code");

        String accessToken = authService.requestAccessToken(code);
        authService.requestUserInfo(accessToken);
    }
}
