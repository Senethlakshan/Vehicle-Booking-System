package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.model.Department;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Reaction;

import java.time.LocalDateTime;
import java.util.Set;


public record CreateEventResponseDTO(
        Long id,
        String title,
        String description,
        Set<Department> departments,
        BlobResponseDTO imageBlob,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme
) {
}
