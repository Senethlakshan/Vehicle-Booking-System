package com.jxg.isn_backend.dto.response;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)


public record StoryResponseDTO( Long id,
                                String description,
                                String imagePath,
                                Long userId,
                                LocalDateTime createdAt,
                                String userFirstName,
                                String userImageUrl,
                                String message,
                                String error) {

}
