//package com.jxg.isn_backend.service;
//
//import com.jxg.isn_backend.model.Comment;
//import com.jxg.isn_backend.model.Post;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.repository.CommentRepository;
//import com.jxg.isn_backend.repository.PostRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private AuthService authService;
//
//    // Create a comment for a post by the currently logged-in user
//    public Comment addCommentToPost(Long postId, String text) {
//        User currentUser = authService.getCurrentLoggedUser();
//        Optional<Post> postOptional = postRepository.findById(postId);
//
//        if (postOptional.isEmpty()) {
//            throw new RuntimeException("Post not found.");
//        }
//
//        Post post = postOptional.get();
//
//        Comment comment = new Comment();
//        comment.setText(text);
//        comment.setPost(post);
//        comment.setCreatedBy(currentUser);
//        // Comment will be linked to the current user via AuthService
//
//        return commentRepository.save(comment);
//    }
//
//    // Get a comment by ID
//    public Comment getCommentById(Long commentId) {
//        return commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found."));
//    }
//
//    // Delete a comment by ID
//    public void deleteComment(Long commentId) {
//        if (!commentRepository.existsById(commentId)) {
//            throw new RuntimeException("Comment not found.");
//        }
//        commentRepository.deleteById(commentId);
//    }
//
//    public List<Comment> getCommentsByPostId(Long postId) {
//        return commentRepository.findByPostId(postId);
//    }
//}



//
//package com.jxg.isn_backend.service;
//
//import com.jxg.isn_backend.dto.response.CommentResponseDTO;
//import com.jxg.isn_backend.dto.response.UserDetailsResponseDTO;
//import com.jxg.isn_backend.model.Comment;
//import com.jxg.isn_backend.model.Post;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.repository.CommentRepository;
//import com.jxg.isn_backend.repository.PostRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private AuthService authService;
//
//    public Comment addCommentToPost(Long postId, String text) {
//        User currentUser = authService.getCurrentLoggedUser();
//        Optional<Post> postOptional = postRepository.findById(postId);
//
//        if (postOptional.isEmpty()) {
//            throw new RuntimeException("Post not found.");
//        }
//
//        Post post = postOptional.get();
//
//        Comment comment = new Comment();
//        comment.setText(text);
//        comment.setPost(post);
//        comment.setCreatedBy(currentUser);
//
//        return commentRepository.save(comment);
//
//    }
//
//    public Comment getCommentById(Long commentId) {
//        return commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found."));
//    }
//
//    public void deleteComment(Long commentId) {
//        if (!commentRepository.existsById(commentId)) {
//            throw new RuntimeException("Comment not found.");
//        }
//        commentRepository.deleteById(commentId);
//    }
//
////    public List<Comment> getCommentsByPostId(Long postId) {
////        return commentRepository.findByPostId(postId);
////    }
//
//    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
//        List<Comment> comments = commentRepository.findByPostId(postId);
//
//        return comments.stream()
//                .map(this::mapToCommentResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Helper method to map Comment to CommentResponseDTO
//    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
//        User createdByUser = comment.getCreatedBy();
//        UserDetailsResponseDTO userDetailsResponseDTO = new UserDetailsResponseDTO(
//                createdByUser.getId(),
//                createdByUser.getFirstName(),
//                createdByUser.getLastName(),
//                createdByUser.getEmail()
//        );
//
//        return new CommentResponseDTO(
//                comment.getId(),
//                comment.getText(),
//                userDetailsResponseDTO,
//                comment.getPost().getId()
//        );
//    }
//}


package com.jxg.isn_backend.service;

import com.jxg.isn_backend.dto.response.CommentResponseDTO;
import com.jxg.isn_backend.dto.response.UserDetailsResponseDTO;
import com.jxg.isn_backend.model.Comment;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.CommentRepository;
import com.jxg.isn_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;

    public CommentResponseDTO addCommentToPost(Long postId, String text) {
        User currentUser = authService.getCurrentLoggedUser();
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post not found.");
        }

        Post post = postOptional.get();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setPost(post);
        comment.setCreatedBy(currentUser);

        Comment savedComment = commentRepository.save(comment);

        return mapToCommentResponseDTO(savedComment);
    }

    public CommentResponseDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found."));
        return mapToCommentResponseDTO(comment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found.");
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::mapToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        User createdByUser = comment.getCreatedBy();
        UserDetailsResponseDTO userDetailsResponseDTO = new UserDetailsResponseDTO(
                createdByUser.getId(),
                createdByUser.getFirstName(),
                createdByUser.getLastName(),
                createdByUser.getEmail(),
                createdByUser.getImageUrl()
        );

        return new CommentResponseDTO(
                comment.getId(),
                comment.getText(),
                userDetailsResponseDTO,
                comment.getPost().getId()
        );
    }
}
