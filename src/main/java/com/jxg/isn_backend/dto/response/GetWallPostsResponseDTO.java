package com.jxg.isn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jxg.isn_backend.model.FileBlob;

import java.util.Set;

public record GetWallPostsResponseDTO(
        Long id,
        Long userId,
        FileBlob profileImageBlob,
        String imageUrl,
        String firstName,
        String lastName,
        String designation,
        String designationUrl,
        String Role,
        String title,
        String description,
        @JsonProperty("blobs")
        Set<BlobResponseDTO> blobSet,
        Long likeCount,
        Boolean isLoggedUserReacted

) {
}
