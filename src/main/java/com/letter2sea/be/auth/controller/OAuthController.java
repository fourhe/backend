package com.letter2sea.be.auth.controller;

import static com.letter2sea.be.auth.util.OAuthUtils.ACCESS_TOKEN;
import static com.letter2sea.be.auth.util.OAuthUtils.REFRESH_TOKEN;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.auth.oauth.OAuthProperties;
import com.letter2sea.be.auth.service.LoginService;
import com.letter2sea.be.auth.service.OAuthService;
import com.letter2sea.be.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthProperties oauthProperties;
    private final JwtProvider jwtProvider;

    private final OAuthService oauthService;
    private final LoginService loginService;

    @GetMapping("/login/form")
    public void redirectLoginForm(HttpServletResponse response) throws IOException {
        response.sendRedirect(oauthProperties.getLoginFormUrl());
    }

    @PostMapping("/login")
    public void login(@RequestBody Map<String, String> map, HttpServletResponse response) {
        String code = map.get("code");

        String accessToken = oauthService.requestAccessToken(code);
        Member oauthMember = oauthService.requestUserInfo(accessToken);
        Long memberId = loginService.login(oauthMember);

        log.info("login member id = {}", memberId);

        String jwtAccessToken = jwtProvider.issueAccessToken(memberId);
        String jwtRefreshToken = jwtProvider.issueRefreshToken(memberId);
        loginService.updateRefreshToken(jwtRefreshToken, memberId);

        response.setHeader(ACCESS_TOKEN, jwtAccessToken);
        response.setHeader(REFRESH_TOKEN, jwtRefreshToken);

        log.info("login ok.. accessToken = {}", jwtAccessToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest servletRequest) {
        String refreshToken = servletRequest.getHeader(REFRESH_TOKEN);
        log.info("login refresh token = {}", refreshToken);

        Long memberId = jwtProvider.decode(refreshToken);
        log.info("logout member id = {}", memberId);

        loginService.deleteRefreshToken(memberId);
    }

    @GetMapping("/reissue/access-token")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        String reissuedAccessToken = jwtProvider.issueAccessToken(decodedMemberId);

        response.setHeader(ACCESS_TOKEN, reissuedAccessToken);
    }
}
