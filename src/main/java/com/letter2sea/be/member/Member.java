package com.letter2sea.be.member;

import com.letter2sea.be.common.util.BaseTimeEntity;
import com.letter2sea.be.mailbox.domain.MailBox;
import com.letter2sea.be.trash.domain.Trash;
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

    private String email;
    private String nickname;
    private String refreshToken;
    private int thankCount;

    @OneToMany(mappedBy = "member")
    private List<MailBox> mailBoxes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Trash> TrashList = new ArrayList<>();

    @Builder
    public Member(Long kakaoId, String email, String nickname, String refreshToken) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.thankCount = 0;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void increaseThankCount() {
        this.thankCount++;
    }
}
