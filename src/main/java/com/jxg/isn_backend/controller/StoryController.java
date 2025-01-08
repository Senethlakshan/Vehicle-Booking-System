package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.dto.response.StoryResponseDTO;
import com.jxg.isn_backend.model.Story;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.StoryRepository;
import com.jxg.isn_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_IMAGES = 4;
    private int maxVideoDurationSeconds = 30;
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/add")
    public ResponseEntity<List<StoryResponseDTO>> addStory(
            @RequestParam("description") String description,
            @RequestParam("files") List<MultipartFile> files) {

        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user = optionalUser.orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        if (files.size() > MAX_IMAGES) {
            return ResponseEntity.badRequest()
                    .body(List.of(new StoryResponseDTO(null, null, null, null, null, null, null,"Cannot upload more than " + MAX_IMAGES + " files.", null)));
        }

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(List.of(new StoryResponseDTO(null, null, null, null, null, null, null,"One or more files are empty", null)));
            }

            try {
                // Save file to external directory
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Process video files
                if (isVideo(file)) {
                    if (!validateVideoDurationAndEnhanceQuality(filePath.toString())) {
                        return ResponseEntity.badRequest()
                                .body(List.of(new StoryResponseDTO(null, null, null, null, null, null, null,"Video exceeds 30 seconds or failed to enhance quality", null)));
                    }
                }

                // Generate file URL
                String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/")
                        .path(fileName)
                        .toUriString();
                fileUrls.add(fileUrl);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(List.of(new StoryResponseDTO(null, null, null, null, null, null, null,"Error saving file: " + e.getMessage(), null)));
            }
        }

        // Save stories in database with file URLs
        List<Story> stories = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            Story story = new Story();
            story.setDescription(description);
            story.setImagePath(fileUrl);
            story.setCreatedAt(LocalDateTime.now());
            story.setUser(user);
            stories.add(storyRepository.save(story));
        }

        // Convert stories to DTOs
        List<StoryResponseDTO> responseList = stories.stream()
                .map(story -> new StoryResponseDTO(
                        story.getId(), story.getDescription(), story.getImagePath(),
                        story.getUser().getId(), story.getCreatedAt(), story.getUser().getFirstName(),story.getUser().getImageUrl(),
                        "Story added successfully", null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    private boolean isVideo(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("video/");
    }

    private boolean validateVideoDurationAndEnhanceQuality(String filePath) {
        try {
            String ffmpegPath = "/usr/local/bin/ffmpeg"; // Path to ffmpeg executable
            String processedFilePath = filePath + "_processed.mp4";
            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath, "-i", filePath, "-t", String.valueOf(maxVideoDurationSeconds),
                    "-b:v", "1M", "-vcodec", "libx264", "-acodec", "aac", "-y", processedFilePath
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return false;
            }

            Path processedPath = Paths.get(processedFilePath);
            Files.move(processedPath, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping
    public ResponseEntity<List<StoryResponseDTO>> getAllStories() {
        List<Story> stories = storyRepository.findAll();

        if (stories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }

        List<StoryResponseDTO> responseList = stories.stream()
                .map(story -> new StoryResponseDTO(
                        story.getId(), story.getDescription(), story.getImagePath(),
                        story.getUser().getId(), story.getCreatedAt(), story.getUser().getFirstName(),    story.getUser().getImageUrl(),
                        "Story fetched successfully", null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/my-stories")
    public ResponseEntity<List<StoryResponseDTO>> getMyStories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);

        if (user != null) {
            List<Story> stories = storyRepository.findByUserId(user.getId());
            List<StoryResponseDTO> responseList = stories.stream()
                    .map(story -> new StoryResponseDTO(
                            story.getId(), story.getDescription(), story.getImagePath(),
                            story.getUser().getId(), story.getCreatedAt(), story.getUser().getFirstName(),    story.getUser().getImageUrl(),
                            "Story fetched successfully", null))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> deleteStory(@PathVariable("id") Long id) {
        Optional<Story> story = storyRepository.findById(id);

        if (story.isPresent()) {
            try {
                File file = new File(story.get().getImagePath());
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

            storyRepository.deleteById(id);
            return ResponseEntity.ok(new StoryResponseDTO(
                    id, story.get().getDescription(), story.get().getImagePath(),
                    story.get().getUser().getId(), story.get().getCreatedAt(), story.get().getUser().getFirstName(),    story.get().getUser().getImageUrl(),
                    "Story deleted successfully.", null
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StoryResponseDTO(null, null, null, null, null, null,null,"Story not found.", null));
        }
    }

    @GetMapping("/user/{userId}/first")
    public ResponseEntity<StoryResponseDTO> getFirstStoryByUserId(@PathVariable Long userId) {
        List<Story> stories = storyRepository.findByUserId(userId);

        if (stories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StoryResponseDTO(null, null, null, null, null, null,null,"No stories found for the user.", null));
        }

        Story firstStory = stories.get(0);

        return ResponseEntity.ok(new StoryResponseDTO(
                firstStory.getId(), firstStory.getDescription(), firstStory.getImagePath(),
                firstStory.getUser().getId(), firstStory.getCreatedAt(), firstStory.getUser().getFirstName(), firstStory.getUser().getImageUrl(),
                "Story fetched successfully", null
        ));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StoryResponseDTO>> getStoriesByUserId(@PathVariable Long userId) {
        List<Story> stories = storyRepository.findByUserId(userId);

        if (stories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }

        List<StoryResponseDTO> responseList = stories.stream()
                .map(story -> new StoryResponseDTO(
                        story.getId(), story.getDescription(), story.getImagePath(),
                        story.getUser().getId(), story.getCreatedAt(), story.getUser().getFirstName(),    story.getUser().getImageUrl(),
                        "Story fetched successfully", null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/other")
    public ResponseEntity<List<StoryResponseDTO>> getOtherUsersStories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        String username = authentication.getName();
        User currentUser = userRepository.findByEmail(username).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        List<Story> stories = storyRepository.findByUserIdNot(currentUser.getId());

        if (stories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }

        List<StoryResponseDTO> responseList = stories.stream()
                .map(story -> new StoryResponseDTO(
                        story.getId(), story.getDescription(), story.getImagePath(),
                        story.getUser().getId(), story.getCreatedAt(), story.getUser().getFirstName(),    story.getUser().getImageUrl(),
                        "Story fetched successfully", null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

//    @Scheduled(fixedRate = 86400000)
//    public void deleteExpiredStories() {
//        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
//        LocalDateTime threshold = now.minusDays(1);
//
//        List<Story> expiredStories = storyRepository.findByCreatedAtBefore(threshold);
//
//        for (Story story : expiredStories) {
//            try {
//                File file = new File(story.getImagePath());
//                if (file.exists()) {
//                    if (file.delete()) {
//                        System.out.println("File deleted: " + file.getAbsolutePath());
//                    } else {
//                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("Error deleting file: " + e.getMessage());
//            }
//
//            storyRepository.delete(story);
//        }
//    }
}
