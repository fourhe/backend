package com.letter2sea.be.auth.oauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.letter2sea.be.member.Member;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthUserInfo {

    private Long id;
    private Properties properties;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
        private String email;
    }

    public Member toMember() {
        return Member.builder()
            .kakaoId(id)
            .nickname(getProperties().nickname)
            .email(getProperties().email)
            .build();
    }
}
