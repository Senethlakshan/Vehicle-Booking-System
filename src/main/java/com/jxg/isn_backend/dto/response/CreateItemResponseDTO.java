package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Reaction;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateItemResponseDTO(
        Long id,
        String name,
        String description,
        Integer categoryId,
        Integer subCategoryId,
        Set<FileBlob> fileBlobs,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme
) {
}
