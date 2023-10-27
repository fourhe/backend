package com.letter2sea.be.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letter2sea.be.auth.oauth.OAuthProperties;
import com.letter2sea.be.auth.oauth.OAuthUserInfo;
import com.letter2sea.be.auth.oauth.OauthAccessToken;
import com.letter2sea.be.auth.oauth.dto.OAuthAccessTokenRequest;
import com.letter2sea.be.common.converter.MultiValueMapConverter;
import com.letter2sea.be.member.Member;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final WebClient webClient;
    private final OAuthProperties oauthProperties;


    public String requestAccessToken(String code) {
        OauthAccessToken oauthAccessToken = webClient
            .post()
            .uri(oauthProperties.getAccessTokenApiUrl())
            .headers(header -> {
                header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            })
            .bodyValue(MultiValueMapConverter.convert(
                new ObjectMapper(), OAuthAccessTokenRequest.of(code, oauthProperties)))
            .retrieve()
            .bodyToMono(OauthAccessToken.class)
            .block();

        return oauthAccessToken.getAccessToken();
    }

    public Member requestUserInfo(String accessToken) {
        OAuthUserInfo oauthUserInfo = webClient
            .get()
            .uri(oauthProperties.getUserApiUrl())
            .headers(header -> header.setBearerAuth(accessToken))
            .retrieve()
            .bodyToMono(OAuthUserInfo.class)
            .block();

        return oauthUserInfo.toMember();
    }

}
