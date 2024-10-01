package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;

import java.time.LocalDateTime;

public record UpdatePostResponseDTO(
        Long id,
        String title,
        String description,
        UserMinDTO updatedBy,
        LocalDateTime lastModifiedDatetime
) {
}
