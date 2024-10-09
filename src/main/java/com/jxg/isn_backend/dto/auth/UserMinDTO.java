package com.jxg.isn_backend.dto.auth;

import com.jxg.isn_backend.model.FileBlob;

public record UserMinDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String imageUrl,
        FileBlob imageBlob,
        String designation,
        String designationUrl,
        String role
) {
}
