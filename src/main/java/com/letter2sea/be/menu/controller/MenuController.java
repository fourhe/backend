package com.letter2sea.be.menu.controller;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.menu.dto.MenuInfoResponse;
import com.letter2sea.be.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final JwtProvider jwtProvider;
    private final MenuService menuService;

    @GetMapping("/info")
    public MenuInfoResponse getMenuInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        return menuService.getMenuInfo(memberId);
    }
}
