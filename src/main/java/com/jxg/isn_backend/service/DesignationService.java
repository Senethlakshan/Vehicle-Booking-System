package com.jxg.isn_backend.service;

import com.jxg.isn_backend.model.Designation;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DesignationService {

    private static final String UPLOAD_DIR = "uploads/designation";

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public Designation createDesignation(String name) {
        User createdByUser = authService.getCurrentLoggedUser();

        Designation designation = new Designation();
        designation.setName(name);
        designation.setCreatedBy(createdByUser);

        return designationRepository.save(designation);
    }
    // Get all designations
    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    // Get a specific designation by ID
    public Optional<Designation> getDesignationById(Integer id) {
        return designationRepository.findById(id);
    }

    // Delete a designation by ID
    public void deleteDesignation(Integer id) {
        designationRepository.deleteById(id);
    }

    @Transactional
    public ResponseEntity<String> uploadImageForDesignation(Integer id, MultipartFile file) {
        Optional<Designation> optionalDesignation = designationRepository.findById(id);
        if (optionalDesignation.isEmpty()) {
            return ResponseEntity.notFound().build(); // Designation not found
        }

        Designation designation = optionalDesignation.get();

        try {
            // Save the file to the specified directory
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Generate file URL
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/designation/images/")
                    .path(fileName)
                    .toUriString();

            designation.setImageUrl(fileUrl);
            designationRepository.save(designation); // Save updated designation

            return ResponseEntity.ok(fileUrl); // Return the URL of the uploaded image
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving image: " + e.getMessage());
        }
    }

}
