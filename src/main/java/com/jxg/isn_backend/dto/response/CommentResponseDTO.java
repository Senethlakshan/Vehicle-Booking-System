package com.jxg.isn_backend.dto.response;

public class CommentResponseDTO {
    private Long id;
    private String text;
    private UserDetailsResponseDTO createdBy;
    private Long postId;

    public CommentResponseDTO(Long id, String text, UserDetailsResponseDTO createdBy, Long postId) {
        this.id = id;
        this.text = text;
        this.createdBy = createdBy;
        this.postId = postId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDetailsResponseDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDetailsResponseDTO createdBy) {
        this.createdBy = createdBy;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
