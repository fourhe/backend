package com.letter2sea.be.trash.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TrashPaginatedResponse {

    private final int currentPage;
    private final int pageSize;
    private final long totalPages;
    private final List<TrashListResponse> trashListResponses;

    public TrashPaginatedResponse(int currentPage, int pageSize, long totalPages,
        List<TrashListResponse> trashListResponses) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.trashListResponses = trashListResponses;
    }
}
