package com.jxg.isn_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Post extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

//    @OneToMany(mappedBy = "post")
//    private Set<Comment> comments;
//
//    @OneToMany(mappedBy = "post")
//    private Set<Reaction> reactions;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "post_id")
//    private Set<FileBlob>fileBlobs;

    // Cascade delete for comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    // Cascade delete for reactions
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions;

    // Cascade delete for saved posts
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Saved> savedPosts;

    // Cascade delete for file blobs
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Set<FileBlob> fileBlobs;
}
