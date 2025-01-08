package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Reaction;
import com.jxg.isn_backend.model.User;

import java.time.LocalDateTime;
import java.util.Set;

public record CreatePostResponseDTO(
        Long id,
        String title,
        String description,
        Set<Reaction> reactions,
        Set<FileBlob> fileBlobs,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme

) {
}
