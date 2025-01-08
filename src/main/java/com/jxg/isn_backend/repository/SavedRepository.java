package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.model.Item;
import com.jxg.isn_backend.model.Post;
import com.jxg.isn_backend.model.Saved;
import com.jxg.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedRepository extends JpaRepository<Saved, Long> {

    List<Saved> findByCreatedBy(User user);

    Optional<Saved> findByPostAndCreatedBy(Post post, User user);

    Optional<Saved> findByItemAndCreatedBy(Item item, User user);
    void deleteAllByPost(Post post);

    void deleteAllByItem(Item item);
}
