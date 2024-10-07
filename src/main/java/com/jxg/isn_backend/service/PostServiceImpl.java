//package com.jxg.isn_backend.service;
//import org.springframework.transaction.annotation.Transactional;
//import com.jxg.isn_backend.dto.auth.UserMinDTO;
//import com.jxg.isn_backend.dto.request.CreatePostRequestDTO;
//import com.jxg.isn_backend.dto.response.CreatePostResponseDTO;
//import com.jxg.isn_backend.dto.response.GetWallPostsResponseDTO;
//import com.jxg.isn_backend.mapper.BlobMapper;
//import com.jxg.isn_backend.model.FileBlob;
//import com.jxg.isn_backend.model.Post;
//import com.jxg.isn_backend.model.Reaction;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.repository.CommentRepository;
//import com.jxg.isn_backend.repository.PostRepository;
//import com.jxg.isn_backend.repository.ReactionRepository;
//import com.jxg.isn_backend.repository.SavedRepository;
//import jakarta.persistence.criteria.Predicate;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class PostServiceImpl implements PostService {
//
//    private final BlobService blobService;
//    private final PostRepository postRepository;
//    private final AuthService authService;
//    private final SavedRepository savedRepository;
//    private final CommentRepository commentRepository;
//    private final ReactionRepository reactionRepository;
//
//    @Value("${spring.app.localBlobDirectory}")
//    private String File_DIRECTORY;
//
//    public PostServiceImpl(
//       BlobService blobService,
//            PostRepository postRepository,
//                           SavedRepository savedRepository,
//                           CommentRepository commentRepository,
//                           ReactionRepository reactionRepository,
//                           AuthService authService) {
//        this.postRepository = postRepository;
//        this.savedRepository = savedRepository;
//        this.commentRepository = commentRepository;
//        this.reactionRepository = reactionRepository;
//        this.authService = authService;
//        this.blobService= blobService;
//    }
//
//    @Override
//    public CreatePostResponseDTO createPost(CreatePostRequestDTO requestDTO, MultipartFile[] files) throws IOException {
//
//        Set<FileBlob> fileBlobSet = new HashSet<>();
//        Post post = new Post();
//
//
//        if (files != null) {
//            for (MultipartFile file : files) {
////                var blob = blobRepository.save(blobService.saveBlobToLocal(File_DIRECTORY, file));
//                FileBlob blob = blobService.saveBlobToLocal(File_DIRECTORY, file);
//                fileBlobSet.add(blob);
//            }
//        } else {
//            fileBlobSet = null;
//        }
//
//
//        post.setTitle(requestDTO.title());
//        post.setFileBlobs(fileBlobSet);
//        post.setDescription(requestDTO.description());
//
//        post = postRepository.save(post);
//
//        var createdUserMinDTO = new UserMinDTO(post.getCreatedBy().getId(), post.getCreatedBy().getEmail(), post.getCreatedBy().getFirstName(), post.getCreatedBy().getLastName(),post.getCreatedBy().getImageUrl() ,post.getCreatedBy().getImageBlob(),post.getCreatedBy().getDesignation().getName());   //remove
//        var updatedUserMinDTO = new UserMinDTO(post.getLastModifiedBy().getId(), post.getLastModifiedBy().getEmail(), post.getLastModifiedBy().getFirstName(), post.getLastModifiedBy().getLastName(), post.getCreatedBy().getImageUrl() ,post.getLastModifiedBy().getImageBlob(),post.getCreatedBy().getDesignation().getName());
//
//
//        return new CreatePostResponseDTO(
//                post.getId(),
//                post.getTitle(),
//                post.getDescription(),
//                post.getReactions(),
//                post.getFileBlobs(),
//                createdUserMinDTO,
//                updatedUserMinDTO,
//                post.getCreatedDatetime(),
//                post.getLastModifiedDatetime()
//        );
//
//    }
//
//    @Override
//    public Page<GetWallPostsResponseDTO> getWallPosts(Pageable pageable) {
//
//        Page<Post> postPage = this.postRepository.findAll(pageable);
//        List<GetWallPostsResponseDTO> postsResponseDTOs = new ArrayList<>();
//
//        for (Post post : postPage.stream().toList()) {
//
//            postsResponseDTOs.add(new GetWallPostsResponseDTO(
//                    post.getId(),
//                    post.getCreatedBy().getId(),
//                    post.getCreatedBy().getImageBlob(),
//                    post.getCreatedBy().getImageUrl(),
//                    post.getCreatedBy().getFirstName(),
//                    post.getCreatedBy().getLastName(),
//                    post.getCreatedBy().getDesignation().getName(),
//                    post.getTitle(),
//                    post.getDescription(),
//                    post.getFileBlobs() != null
//                            ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet())
//                            : new HashSet<>(),
//                    (long) post.getReactions().size(),
//                    this.checkIsCurrentUserReacted(post)
//                    ));
//        }
//
//
//        return new PageImpl<>(postsResponseDTOs, postPage.getPageable(), postPage.getTotalElements());
//    }
//
//
//    @Override
//    public Page<GetWallPostsResponseDTO> searchPosts(Pageable pageable, String searchText) {
//
//
//        Specification<Post> spec = (root, query, builder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (StringUtils.isNotBlank(searchText)) {
//                predicates.add(builder.or(
//                        builder.like(root.get("createdBy").get("firstName"), "%" + searchText + "%"),
//                        builder.like(root.get("createdBy").get("lastName"), "%" + searchText + "%"),
//                        builder.like(root.get("title"), "%" + searchText + "%"),
//                        builder.like(root.get("description"), "%" + searchText + "%")
//                ));
//            }
//
//            return builder.and(predicates.toArray(new Predicate[0]));
//        };
//
//        Page<Post> postPage = postRepository.findAll(spec, pageable);
//        List<GetWallPostsResponseDTO> result = new ArrayList<>();
//
//        for (Post post : postPage.stream().toList()) {
//
//            result.add(new GetWallPostsResponseDTO(
//                    post.getId(),
//                    post.getCreatedBy().getId(),
//                    post.getCreatedBy().getImageBlob(),
//                    post.getCreatedBy().getImageUrl(),
//                    post.getCreatedBy().getFirstName(),
//                    post.getCreatedBy().getLastName(),
//                    post.getCreatedBy().getDesignation().getName(),
//                    post.getTitle(),
//                    post.getDescription(),
//                    post.getFileBlobs() != null
//                            ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet())
//                            : new HashSet<>(),
//                    (long) post.getReactions().size(),
//                    this.checkIsCurrentUserReacted(post)
//                    ));
//        }
//
//
//        return new PageImpl<>(result, postPage.getPageable(), postPage.getTotalElements());
//
//    }
//
//
//    private Boolean checkIsCurrentUserReacted(Post post) {
//
//        Long currentUserId = this.authService.getCurrentLoggedUser().getId();
//
//        for (Reaction reaction : post.getReactions()) {
//            if (reaction.getLastModifiedBy().getId().equals(currentUserId)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public Page<GetWallPostsResponseDTO> getPostsByUser(User user, Pageable pageable) {
//        Page<Post> postPage = postRepository.findAllByCreatedBy(user, pageable);
//        List<GetWallPostsResponseDTO> postsResponseDTOs = new ArrayList<>();
//
//        for (Post post : postPage.getContent()) {
//            postsResponseDTOs.add(new GetWallPostsResponseDTO(
//                    post.getId(),
//                    post.getCreatedBy().getId(),
//                    post.getCreatedBy().getImageBlob(),
//                    post.getCreatedBy().getImageUrl(),
//                    post.getCreatedBy().getFirstName(),
//                    post.getCreatedBy().getLastName(),
//                    post.getCreatedBy().getDesignation().getName(),
//                    post.getTitle(),
//                    post.getDescription(),
//                    post.getFileBlobs() != null
//                            ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet())
//                            : new HashSet<>(),
//                    (long) post.getReactions().size(),
//                    this.checkIsCurrentUserReacted(post)  // Assuming you're checking if the current user reacted to this post
//            ));
//        }
//
//        return new PageImpl<>(postsResponseDTOs, pageable, postPage.getTotalElements());
//    }
//
//
//    @Override
//    @Transactional
//    public void deletePost(Long postId) {
//        // Fetch the post
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//
////        User currentUser = authService.getCurrentLoggedUser();
////        if (!post.getCreatedBy().getId().equals(currentUser.getId())) {
////            throw new RuntimeException("Unauthorized to delete this post");
////        }
//
//        commentRepository.deleteAllByPost(post);
//        reactionRepository.deleteAllByPost(post);
//        savedRepository.deleteAllByPost(post);
//
//        // Finally, delete the post itself
//        postRepository.delete(post);
//    }
//
//
////    @Transactional
////    public void updatePost(Long postId, String title, String description) {
////        // Fetch the existing post
////        Post post = postRepository.findById(postId)
////                .orElseThrow(() -> new RuntimeException("Post not found"));
////
////        // Update the title and description
////        post.setTitle(title);
////        post.setDescription(description);
////
////        // Save the updated post
////        postRepository.save(post);
////    }
//
//    @Transactional
//    public Post updatePost(Long postId, String title, String description) {
//        // Fetch the existing post
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        // Update the title and description
//        post.setTitle(title);
//        post.setDescription(description);
//
//        // Save and return the updated post
//        return postRepository.save(post);
//    }
//
//}

package com.jxg.isn_backend.service;

import org.springframework.transaction.annotation.Transactional;
import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.dto.request.CreatePostRequestDTO;
import com.jxg.isn_backend.dto.response.CreatePostResponseDTO;
import com.jxg.isn_backend.dto.response.GetWallPostsResponseDTO;
import com.jxg.isn_backend.mapper.BlobMapper;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.Reaction;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.CommentRepository;
import com.jxg.isn_backend.repository.PostRepository;
import com.jxg.isn_backend.repository.ReactionRepository;
import com.jxg.isn_backend.repository.SavedRepository;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final BlobService blobService;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final SavedRepository savedRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    @Value("${spring.app.localBlobDirectory}")
    private String fileDirectory;

    public PostServiceImpl(
            BlobService blobService,
            PostRepository postRepository,
            SavedRepository savedRepository,
            CommentRepository commentRepository,
            ReactionRepository reactionRepository,
            AuthService authService) {
        this.blobService = blobService;
        this.postRepository = postRepository;
        this.savedRepository = savedRepository;
        this.commentRepository = commentRepository;
        this.reactionRepository = reactionRepository;
        this.authService = authService;
    }

    @Override
    public CreatePostResponseDTO createPost(CreatePostRequestDTO requestDTO, MultipartFile[] files) throws IOException {
        Set<FileBlob> fileBlobSet = new HashSet<>();

        // Save file blobs
        if (files != null) {
            for (MultipartFile file : files) {
                FileBlob blob = blobService.saveBlobToLocal(fileDirectory, file);
                fileBlobSet.add(blob);
            }
        }

        // Create and save post
        Post post = new Post();
        post.setTitle(requestDTO.title());
        post.setFileBlobs(fileBlobSet);
        post.setDescription(requestDTO.description());
        post = postRepository.save(post);

        // Create UserMinDTOs for response
        User createdBy = post.getCreatedBy();
        User lastModifiedBy = post.getLastModifiedBy();

        var createdUserMinDTO = new UserMinDTO(
                createdBy.getId(),
                createdBy.getEmail(),
                createdBy.getFirstName(),
                createdBy.getLastName(),
                createdBy.getImageUrl(),
                createdBy.getImageBlob(),
                createdBy.getDesignation() != null ? createdBy.getDesignation().getName() : "Department Not Specified",
                createdBy.getDesignation() != null ? createdBy.getDesignation().getImageUrl(): "Department Not Specified"
        );

        var updatedUserMinDTO = new UserMinDTO(
                lastModifiedBy.getId(),
                lastModifiedBy.getEmail(),
                lastModifiedBy.getFirstName(),
                lastModifiedBy.getLastName(),
                lastModifiedBy.getImageUrl(),
                lastModifiedBy.getImageBlob(),
                lastModifiedBy.getDesignation() != null ? lastModifiedBy.getDesignation().getName() : "unknown",
                lastModifiedBy.getDesignation() != null ? lastModifiedBy.getDesignation().getImageUrl():"unknown"
        );

        return new CreatePostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getReactions(),
                post.getFileBlobs(),
                createdUserMinDTO,
                updatedUserMinDTO,
                post.getCreatedDatetime(),
                post.getLastModifiedDatetime()
        );
    }

    @Override
    public Page<GetWallPostsResponseDTO> getWallPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        List<GetWallPostsResponseDTO> postsResponseDTOs = postPage.stream().map(post -> {
            return new GetWallPostsResponseDTO(
                    post.getId(),
                    post.getCreatedBy().getId(),
                    post.getCreatedBy().getImageBlob(),
                    post.getCreatedBy().getImageUrl(),
                    post.getCreatedBy().getFirstName(),
                    post.getCreatedBy().getLastName(),
                    post.getCreatedBy().getDesignation() != null ? post.getCreatedBy().getDesignation().getName() : "Department Not Specified",
                    post.getTitle(),
                    post.getDescription(),
                    post.getFileBlobs() != null ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet()) : new HashSet<>(),
                    (long) post.getReactions().size(),
                    checkIsCurrentUserReacted(post)
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(postsResponseDTOs, postPage.getPageable(), postPage.getTotalElements());
    }

    @Override
    public Page<GetWallPostsResponseDTO> searchPosts(Pageable pageable, String searchText) {
        Specification<Post> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(searchText)) {
                predicates.add(builder.or(
                        builder.like(root.get("createdBy").get("firstName"), "%" + searchText + "%"),
                        builder.like(root.get("createdBy").get("lastName"), "%" + searchText + "%"),
                        builder.like(root.get("title"), "%" + searchText + "%"),
                        builder.like(root.get("description"), "%" + searchText + "%")
                ));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Post> postPage = postRepository.findAll(spec, pageable);
        List<GetWallPostsResponseDTO> result = postPage.stream().map(post -> {
            return new GetWallPostsResponseDTO(
                    post.getId(),
                    post.getCreatedBy().getId(),
                    post.getCreatedBy().getImageBlob(),
                    post.getCreatedBy().getImageUrl(),
                    post.getCreatedBy().getFirstName(),
                    post.getCreatedBy().getLastName(),
                    post.getCreatedBy().getDesignation() != null ? post.getCreatedBy().getDesignation().getName() : "Department Not Specified",
                    post.getTitle(),
                    post.getDescription(),
                    post.getFileBlobs() != null ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet()) : new HashSet<>(),
                    (long) post.getReactions().size(),
                    checkIsCurrentUserReacted(post)
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(result, postPage.getPageable(), postPage.getTotalElements());
    }

    private Boolean checkIsCurrentUserReacted(Post post) {
        Long currentUserId = authService.getCurrentLoggedUser().getId();
        return post.getReactions().stream().anyMatch(reaction -> reaction.getLastModifiedBy().getId().equals(currentUserId));
    }


    @Override
    public Page<GetWallPostsResponseDTO> getPostsByUser(User user, Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByCreatedBy(user, pageable);
        List<GetWallPostsResponseDTO> postsResponseDTOs = postPage.getContent().stream().map(post -> {
            return new GetWallPostsResponseDTO(
                    post.getId(),
                    post.getCreatedBy().getId(),
                    post.getCreatedBy().getImageBlob(),
                    post.getCreatedBy().getImageUrl(),
                    post.getCreatedBy().getFirstName(),
                    post.getCreatedBy().getLastName(),
                    post.getCreatedBy().getDesignation() != null ? post.getCreatedBy().getDesignation().getName() : "Department Not Specified",
                    post.getTitle(),
                    post.getDescription(),
                    post.getFileBlobs() != null ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet()) : new HashSet<>(),
                    (long) post.getReactions().size(),
                    checkIsCurrentUserReacted(post)
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(postsResponseDTOs, pageable, postPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        // Fetch the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Optionally, you could add a check for authorization
        commentRepository.deleteAllByPost(post);
        reactionRepository.deleteAllByPost(post);
        savedRepository.deleteAllByPost(post);

        // Finally, delete the post itself
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, String title, String description) {
        // Fetch the existing post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Update the title and description
        post.setTitle(title);
        post.setDescription(description);

        // Save and return the updated post
        return postRepository.save(post);
    }

    public GetWallPostsResponseDTO getPostById(Long postId) {
        // Fetch the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Create UserMinDTO for response
        User createdBy = post.getCreatedBy();

        var createdUserMinDTO = new UserMinDTO(
                createdBy.getId(),
                createdBy.getEmail(),
                createdBy.getFirstName(),
                createdBy.getLastName(),
                createdBy.getImageUrl(),
                createdBy.getImageBlob(),
                createdBy.getDesignation() != null ? createdBy.getDesignation().getName() : "Department Not Specified",
                createdBy.getDesignation() != null ? createdBy.getDesignation().getImageUrl(): "Department Not Specified"
        );

        return new GetWallPostsResponseDTO(
                post.getId(),
                createdBy.getId(),
                createdBy.getImageBlob(),
                createdBy.getImageUrl(),
                createdBy.getFirstName(),
                createdBy.getLastName(),
                createdBy.getDesignation() != null ? createdBy.getDesignation().getName() : "Department Not Specified",
                post.getTitle(),
                post.getDescription(),
                post.getFileBlobs() != null ? post.getFileBlobs().stream().map(BlobMapper.INSTANCE::toDto).collect(Collectors.toSet()) : new HashSet<>(),
                (long) post.getReactions().size(),
                checkIsCurrentUserReacted(post)
        );
    }
}
