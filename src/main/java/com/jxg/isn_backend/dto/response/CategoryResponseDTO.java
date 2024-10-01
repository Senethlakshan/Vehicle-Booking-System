package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Integer id,
        String name,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme

) {
}
