package com.jxg.isn_backend.dto.request;

import com.jxg.isn_backend.model.Category;
import com.jxg.isn_backend.model.SubCategory;

public record CreateEventRequestDTO(
        String description,
        String title,
        SubCategory subCategory,
        Category category
) {
}