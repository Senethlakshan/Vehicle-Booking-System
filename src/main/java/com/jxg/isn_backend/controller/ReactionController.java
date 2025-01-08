package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.Reaction;
import com.jxg.isn_backend.service.ReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@CrossOrigin
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> reactToPost(@PathVariable Long postId) {
        Reaction reaction = reactionService.reactToPost(postId);
        if (reaction == null) {
            return ResponseEntity.ok().body("Reaction removed.");
        }
        return ResponseEntity.ok().body(reaction);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> countReactions(@PathVariable Long postId) {
        Long count = reactionService.countReactionsByPost(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/posts/liked")
    public ResponseEntity<List<Post>> getLikedPostsForCurrentUser() {
        List<Post> likedPosts = reactionService.getLikedPostsForCurrentUser();
        return ResponseEntity.ok(likedPosts);
    }
}
