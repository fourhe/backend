package com.letter2sea.be.trash.dto;

import com.letter2sea.be.trash.domain.Trash;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TrashDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime deletedAt;

    public TrashDetailResponse(Trash trash) {
        this.id = trash.getId();
        this.title = trash.getTitle();
        this.content = trash.getContent();
        this.deletedAt = trash.getDeletedAt();
    }
}
