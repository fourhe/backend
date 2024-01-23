package com.letter2sea.be.mailbox.domain;

import com.letter2sea.be.common.util.BaseTimeEntity;
import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Letter letter;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private boolean thanked;

    public MailBox(Letter letter, Member member) {
        this.letter = letter;
        this.member = member;
    }

    public void thanks() {
        this.thanked = true;
        this.letter.thankToWriter();
    }
}
