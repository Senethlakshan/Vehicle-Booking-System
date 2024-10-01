package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.Reaction;
import com.jxg.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByPostAndCreatedBy(Post post, User createdBy);
    Long countByPost(Post post);
    List<Reaction> findByCreatedBy(User createdBy);
    void deleteAllByPost(Post post);
}
