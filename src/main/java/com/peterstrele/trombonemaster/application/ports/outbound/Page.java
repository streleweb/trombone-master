package com.peterstrele.trombonemaster.application.ports.outbound;

import java.util.List;

public record Page<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}