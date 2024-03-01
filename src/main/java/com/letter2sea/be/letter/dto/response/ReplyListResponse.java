package com.letter2sea.be.letter.dto.response;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.mailbox.domain.MailBox;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyListResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;
    private final boolean thanked;
    private final boolean read;

    public ReplyListResponse(Letter letter, MailBox reply) {
        this.id = letter.getId();
        this.title = letter.getTitle();
        this.createdAt = letter.getCreatedAt();

        if (reply == null) {
            this.thanked = false;
            this.read = false;
            return;
        }
        this.thanked = reply.isThanked();
        this.read = true;
    }
}
