package com.jxg.isn_backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jxg.isn_backend.model.Story;
import com.jxg.isn_backend.model.User;


public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUser(User user);
    Story findTopByUserIdOrderByCreatedAtDesc(Long userId);
    List<Story> findByUserId(Long userId);
    List <Story> findByUserIdNot (Long userId);
    void deleteByCreatedAtBefore(LocalDateTime threshold);
    List<Story> findByCreatedAtBefore(LocalDateTime threshold);
}
