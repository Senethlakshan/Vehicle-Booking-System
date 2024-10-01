package com.jxg.isn_backend.dto.response;

import com.jxg.isn_backend.dto.auth.UserDTO;
import com.jxg.isn_backend.model.User;

public class CommentRequestDTO {
    private Long id;
    private String text;
    private Long postId;
    UserDetailsResponseDTO createdBy;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
