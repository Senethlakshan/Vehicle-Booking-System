package com.jxg.isn_backend.service;
import com.jxg.isn_backend.model.Story;
import com.jxg.isn_backend.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class StoryCleanupService {

    @Autowired
    private StoryRepository storyRepository;

    // Runs every 5 sec
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredStories() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
//        LocalDateTime threshold = now.minusHours(24);
        LocalDateTime threshold = now.minusDays(1);

        List<Story> expiredStories = storyRepository.findByCreatedAtBefore(threshold);

        for (Story story : expiredStories) {
            try {
                // Delete associated file from the system
                File file = new File(story.getImagePath());
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("File deleted: " + file.getAbsolutePath());
                    } else {
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error deleting file: " + e.getMessage());
            }

            // Delete story from database
            storyRepository.delete(story);
            System.out.println("Deleted story with ID: " + story.getId());
        }
    }
}
