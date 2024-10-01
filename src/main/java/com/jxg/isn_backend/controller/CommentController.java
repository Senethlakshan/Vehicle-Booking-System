//package com.jxg.isn_backend.controller;
//
//import com.jxg.isn_backend.dto.response.CommentRequestDTO;
//import com.jxg.isn_backend.dto.response.CommentResponseDTO;
//import com.jxg.isn_backend.model.Comment;
//import com.jxg.isn_backend.service.CommentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/comments")
//public class CommentController {
//
//    @Autowired
//    private CommentService commentService;
//
//    // Create a comment on a post
//    @PostMapping("/post/{postId}")
//    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO) {
//        try {
//            Comment comment = commentService.addCommentToPost(postId, requestDTO.getText());
//            return new ResponseEntity<>(comment, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // View a comment by ID
//    @GetMapping("/{commentId}")
//    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
//        try {
//            Comment comment = commentService.getCommentById(commentId);
//            return new ResponseEntity<>(comment, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // Delete a comment by ID
//    @DeleteMapping("/{commentId}")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
//        try {
//            commentService.deleteComment(commentId);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
////    @GetMapping("/post/{postId}")
////    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
////        List<Comment> comments = commentService.getCommentsByPostId(postId);
////        return ResponseEntity.ok(comments);
////    }
//
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId) {
//        List<CommentResponseDTO> comments = commentService.getCommentsByPostId(postId);
//        return ResponseEntity.ok(comments);
//    }
//}


package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.dto.response.CommentRequestDTO;
import com.jxg.isn_backend.dto.response.CommentResponseDTO;
import com.jxg.isn_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponseDTO> addCommentToPost(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO) {
        try {
            CommentResponseDTO comment = commentService.addCommentToPost(postId, requestDTO.getText());
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long commentId) {
        try {
            CommentResponseDTO comment = commentService.getCommentById(commentId);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
}
