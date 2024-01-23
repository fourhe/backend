package com.letter2sea.be.letter.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class LetterPaginatedResponse {

    private final int currentPage;
    private final int pageSize;
    private final long totalPages;
    private final List<LetterListResponse> letterListResponses;

    public LetterPaginatedResponse(int currentPage, int pageSize, long totalPages,
        List<LetterListResponse> letterListResponses) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.letterListResponses = letterListResponses;
    }
}
