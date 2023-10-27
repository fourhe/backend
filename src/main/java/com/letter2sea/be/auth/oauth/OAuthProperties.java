package com.letter2sea.be.auth.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    private final String grantType;
    private final String clientId;
    private final String redirectUrl;
    private final String accessTokenApiUrl;
    private final String loginFormUrl;
    private final String userApiUrl;
}
