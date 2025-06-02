package org.evilincorporated.pineapple.controller.dto;

public record ApiError(
        String status,
        String error,
        String description
) {
}
