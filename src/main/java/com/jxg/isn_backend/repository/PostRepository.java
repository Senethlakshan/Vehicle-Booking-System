package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.dto.response.GetWallPostsResponseDTO;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

//    @Query(value = "SELECT p.* FROM Post p " +
//            "LEFT JOIN User createdBy ON p.created_by = createdBy.id " +
//            "LEFT JOIN User lastModifiedBy ON p.last_modified_by = lastModifiedBy.id " +
//            "WHERE (LOWER(CAST(p.title AS CHAR)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(CAST(p.description AS CHAR)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(createdBy.first_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(createdBy.last_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(lastModifiedBy.first_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(lastModifiedBy.last_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(CONCAT(createdBy.first_name, ' ', createdBy.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(CONCAT(lastModifiedBy.first_name, ' ', lastModifiedBy.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))",
//            nativeQuery = true)
//    Page<Post> searchPosts(@Param("searchTerm") String searchTerm, Pageable pageable);



    @Query("SELECT p FROM Post p JOIN FETCH p.createdBy JOIN FETCH p.lastModifiedBy")
    Page<Post> findAllWithCreatorAndModifier(Pageable pageable);


    @Query("SELECT p FROM Post p JOIN FETCH p.createdBy JOIN FETCH p.lastModifiedBy")
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    Page<Post> findAllByCreatedBy(User user, Pageable pageable);

}
