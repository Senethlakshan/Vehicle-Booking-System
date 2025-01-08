package com.jxg.isn_backend.service;

import com.jxg.isn_backend.dto.request.CreatePostRequestDTO;
import com.jxg.isn_backend.dto.response.CreatePostResponseDTO;
import com.jxg.isn_backend.dto.response.GetWallPostsResponseDTO;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {

    CreatePostResponseDTO createPost(CreatePostRequestDTO requestDTO, MultipartFile[] files) throws IOException;

    Page<GetWallPostsResponseDTO> getWallPosts(Pageable pageable);

    Page<GetWallPostsResponseDTO> searchPosts(Pageable pageable, String searchText);

    Page<GetWallPostsResponseDTO> getPostsByUser(User user, Pageable pageable);

    void deletePost(Long postId);

//    void updatePost(Long postId, String title, String description);

    Post updatePost(Long postId, String title, String description);

    GetWallPostsResponseDTO getPostById(Long postId);
}
