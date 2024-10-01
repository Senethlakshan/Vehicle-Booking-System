package com.jxg.isn_backend.service;

import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.Reaction;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.ReactionRepository;
import com.jxg.isn_backend.repository.PostRepository;
import com.jxg.isn_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public ReactionService(ReactionRepository reactionRepository, PostRepository postRepository, AuthService authService) {
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public Reaction reactToPost(Long postId) {
        User currentUser = authService.getCurrentLoggedUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Check if the user already reacted
        Optional<Reaction> existingReaction = reactionRepository.findByPostAndCreatedBy(post, currentUser);

        if (existingReaction.isPresent()) {
            // If a reaction exists, remove it (unlike)
            reactionRepository.delete(existingReaction.get());
            return null;
        } else {
            // If no reaction exists, add a new one
            Reaction reaction = new Reaction();
            reaction.setPost(post);
            reaction.setCreatedBy(currentUser);
            return reactionRepository.save(reaction);
        }
    }

    public Long countReactionsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return reactionRepository.countByPost(post);
    }

    public List<Post> getLikedPostsForCurrentUser() {
        User currentUser = authService.getCurrentLoggedUser();
        List<Reaction> reactions = reactionRepository.findByCreatedBy(currentUser);
        return reactions.stream()
                .map(Reaction::getPost)
                .collect(Collectors.toList());
    }
}
