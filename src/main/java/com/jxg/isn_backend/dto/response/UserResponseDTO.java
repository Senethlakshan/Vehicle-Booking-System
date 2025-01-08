package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.model.Designation;
import com.jxg.isn_backend.model.Role;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        BlobResponseDTO imageBlob,
        Role role,
        Designation designation
) {
}
