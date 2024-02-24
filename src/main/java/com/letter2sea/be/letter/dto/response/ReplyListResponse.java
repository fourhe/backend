package com.letter2sea.be.letter.dto.response;

import com.letter2sea.be.letter.domain.Letter;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyListResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;
    private final boolean thanked;

    public ReplyListResponse(Letter letter, boolean thanked) {
        this.id = letter.getId();
        this.title = letter.getTitle();
        this.createdAt = letter.getCreatedAt();
        this.thanked = thanked;
    }
}
