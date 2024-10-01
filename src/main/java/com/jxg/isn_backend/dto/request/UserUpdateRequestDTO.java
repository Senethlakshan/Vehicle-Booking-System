package com.jxg.isn_backend.dto.request;

import com.jxg.isn_backend.model.Designation;

public record UserUpdateRequestDTO(
        String firstName,
        String lastName,
        Designation designation
) {
}
