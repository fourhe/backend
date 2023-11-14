package com.letter2sea.be.letter.dto;

import com.letter2sea.be.letter.domain.Letter;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterDetailResponse {

    private final Long id;
    private final String title;
    private final String content;

    private final LocalDateTime createdAt;

    public LetterDetailResponse(Letter letter) {
        this.id = letter.getId();
        this.title = letter.getTitle();
        this.content = letter.getContent();
        this.createdAt = letter.getCreatedAt();
    }
}
