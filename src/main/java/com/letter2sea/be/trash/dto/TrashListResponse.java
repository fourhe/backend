package com.letter2sea.be.trash.dto;

import com.letter2sea.be.trash.domain.Trash;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TrashListResponse {

    private final Long id;

    private final Long letterId;
    private final String title;
    private final LocalDateTime deletedAt;

    public TrashListResponse(Trash trash) {
        this.id = trash.getId();
        this.letterId = trash.getLetterId();
        this.title = trash.getTitle();
        this.deletedAt = trash.getDeletedAt();
    }
}
