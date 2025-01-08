package com.jxg.isn_backend.mapper;

import com.jxg.isn_backend.dto.request.CreatePostRequestDTO;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Post;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring")
public abstract class PostMapper {

    public Post requestToPost(CreatePostRequestDTO dto, Set<FileBlob> fileBlobSet) {
        Post post = new Post();

        post.setFileBlobs(fileBlobSet);
        post.setTitle(dto.title());
        post.setDescription(dto.description());

        return post;

    }

}
