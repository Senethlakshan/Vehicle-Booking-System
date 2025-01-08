package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.dto.request.CreatePostRequestDTO;
import com.jxg.isn_backend.dto.request.UpdatePostRequestDTO;
import com.jxg.isn_backend.dto.response.CreatePostResponseDTO;
import com.jxg.isn_backend.dto.response.GetWallPostsResponseDTO;
import com.jxg.isn_backend.dto.response.UpdatePostResponseDTO;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.service.AuthService;
import com.jxg.isn_backend.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final AuthService authService;

//    public PostController(PostService postService) {
//        this.postService = postService;
//    }
public PostController(PostService postService, AuthService authService) {
    this.postService = postService;
    this.authService = authService;
}
    @GetMapping
    public ResponseEntity<Page<GetWallPostsResponseDTO>> getAllPosts(@RequestParam(defaultValue = "0") int pageNo,
                                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                                     @RequestParam(defaultValue = "lastModifiedDatetime") String sortBy,
                                                                     @RequestParam(defaultValue = "DESC") String sortDirection) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDirection), sortBy);
        return ResponseEntity.ok(this.postService.getWallPosts(pageable));
    }

    @PostMapping
    public ResponseEntity<CreatePostResponseDTO> createPost(@RequestPart("data")CreatePostRequestDTO requestDTO, @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {

        CreatePostResponseDTO createdPost = postService.createPost(requestDTO, files);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdPost.id()).toUri())
                .body(createdPost);
    }

//    @GetMapping("/search")
//    public ResponseEntity<Page<GetWallPostsResponseDTO>> searchPosts(@RequestParam(defaultValue = "0") int pageNo,
//                                                                     @RequestParam(defaultValue = "10") int pageSize,
//                                                                     @RequestParam(defaultValue = "lastModifiedDatetime") String sortBy,
//                                                                     @RequestParam(defaultValue = "DESC") String sortDirection,
//                                                                     @RequestParam(name = "s", defaultValue = "") String searchText) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDirection), sortBy);
//        return ResponseEntity.ok(this.postService.searchPosts(pageable, searchText));
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<GetWallPostsResponseDTO>> searchPosts(@RequestParam(defaultValue = "0") int pageNo,
                                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                                     @RequestParam(defaultValue = "title") String sortBy,
                                                                     @RequestParam(defaultValue = "DESC") String sortDirection,
                                                                     @RequestParam(name = "s", defaultValue = "") String searchText) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDirection), sortBy);
        return ResponseEntity.ok(this.postService.searchPosts(pageable, searchText));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<GetWallPostsResponseDTO>> getUserPosts(@RequestParam(defaultValue = "0") int pageNo,
                                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                                      @RequestParam(defaultValue = "lastModifiedDatetime") String sortBy,
                                                                      @RequestParam(defaultValue = "DESC") String sortDirection) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDirection), sortBy);

        // Get the currently authenticated user
        User currentUser = authService.getCurrentLoggedUser();

        // Fetch posts for the logged-in user
        Page<GetWallPostsResponseDTO> userPosts = postService.getPostsByUser(currentUser, pageable);

        return ResponseEntity.ok(userPosts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updatePost(
//            @PathVariable Long id,
//            @RequestBody UpdatePostRequestDTO updatePostRequest) {
//
//
//        postService.updatePost(id, updatePostRequest.getTitle(), updatePostRequest.getDescription());
//
//        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequestDTO updatePostRequest) {

        // Perform the update
        Post updatedPost = postService.updatePost(id, updatePostRequest.getTitle(), updatePostRequest.getDescription());

        // Get the details of the user who updated the post
        User currentUser = authService.getCurrentLoggedUser();
        UserMinDTO updatedByUserDTO = new UserMinDTO(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getImageUrl(),
                currentUser.getImageBlob(),
                currentUser.getDesignation().getName(),
                currentUser.getDesignation().getImageUrl(),
                currentUser.getRole().getAuthority().name()
        );

        // Return the updated post information with the user who updated it
        UpdatePostResponseDTO responseDTO = new UpdatePostResponseDTO(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getDescription(),
                updatedByUserDTO,
                updatedPost.getLastModifiedDatetime()
        );

        return ResponseEntity.ok(responseDTO); // Return 200 OK with the response data
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<GetWallPostsResponseDTO>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "lastModifiedDatetime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(sortDirection), sortBy);

        // Fetch posts for the user with the specified userId
        User user = authService.getUserById(userId);
        Page<GetWallPostsResponseDTO> userPosts = postService.getPostsByUser(user, pageable);

        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetWallPostsResponseDTO> getPostById(@PathVariable Long id) {
        GetWallPostsResponseDTO postResponse = postService.getPostById(id); // Call the service to get the post by ID

        if (postResponse != null) {
            return ResponseEntity.ok(postResponse); // Return the post with HTTP status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Return HTTP status 404 Not Found if the post does not exist
        }
    }

}
