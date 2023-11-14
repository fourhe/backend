package com.letter2sea.be.member;

import com.letter2sea.be.common.util.BaseTimeEntity;
import com.letter2sea.be.mailbox.domain.MailBox;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;

    private String nickname;
    private String refreshToken;

    @OneToMany(mappedBy = "writer")
    private List<MailBox> mailBoxes = new ArrayList<>();

    @Builder
    public Member(Long kakaoId, String nickname, String refreshToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
