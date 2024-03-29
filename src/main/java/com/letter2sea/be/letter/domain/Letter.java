package com.letter2sea.be.letter.domain;

import com.letter2sea.be.common.util.BaseTimeEntity;
import com.letter2sea.be.mailbox.domain.MailBox;
import com.letter2sea.be.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update letter set deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at is null")
public class Letter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @OneToMany(mappedBy = "letter")
    private final List<MailBox> mailBoxes = new ArrayList<>();

    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    private LocalDateTime deletedAt;

    private Long replyLetterId;

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restoreDeletedAt() {
        this.deletedAt = null;
    }

    public void thankToWriter() {
        this.writer.increaseThankCount();
    }
}
