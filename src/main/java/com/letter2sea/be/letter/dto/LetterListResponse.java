package com.letter2sea.be.letter.dto;

import com.letter2sea.be.letter.domain.Letter;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterListResponse {

    private final Long id;
    private final String title;
    private final boolean existsReply;
    private final LocalDateTime createdAt;

    public LetterListResponse(Letter letter) {
        this.id = letter.getId();
        this.title = letter.getTitle();
        this.existsReply = false;
        this.createdAt = letter.getCreatedAt();
    }
}
