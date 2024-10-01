package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.model.Category;
import com.jxg.isn_backend.model.SubCategory;
import com.jxg.isn_backend.model.Tag;

import java.time.LocalDateTime;
import java.util.Set;

public record ItemResponseDTO(
        long id,
        String description,
        String name,
        Boolean isSold,
        Set<Tag> tags,
        BlobResponseDTO imageBlob,
        SubCategory subCategory,
        Category category,
        UserMinDTO createdBy,
        UserMinDTO lastModifiedBy,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt

) {
}
