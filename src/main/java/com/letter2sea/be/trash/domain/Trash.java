package com.letter2sea.be.trash.domain;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Letter letter;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime deletedAt;

    public Trash(Letter letter, Member member) {
        this.title = letter.getTitle();
        this.content = letter.getContent();
        this.letter = letter;
        this.member = member;
        this.deletedAt = LocalDateTime.now();
    }
}
