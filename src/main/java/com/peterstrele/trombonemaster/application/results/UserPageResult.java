package com.peterstrele.trombonemaster.application.results;

import java.util.List;

public record UserPageResult(
        List<RetrievedUser> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}