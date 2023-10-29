package com.letter2sea.be.auth.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.letter2sea.be.auth.oauth.OAuthProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthAccessTokenRequest {

    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String code;

    @Builder
    public OAuthAccessTokenRequest(String grantType, String clientId, String redirectUri,
        String code) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
    }

        public static OAuthAccessTokenRequest of(String code, OAuthProperties oauthProperties) {
        return OAuthAccessTokenRequest.builder()
            .grantType(oauthProperties.getGrantType())
            .clientId(oauthProperties.getClientId())
            .redirectUri(oauthProperties.getRedirectUrl())
            .code(code)
            .build();
    }
}
