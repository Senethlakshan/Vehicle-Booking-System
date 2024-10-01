package com.jxg.isn_backend.dto;

import com.jxg.isn_backend.model.Item;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.User;

public class SavedResponseDTO {
    private Post post;
    private Item item;
    private User createdBy;

    public SavedResponseDTO(Post post, User createdBy) {
        this.post = post;
        this.createdBy = createdBy;
    }

    public SavedResponseDTO(Item item, User createdBy) {
        this.item = item;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
