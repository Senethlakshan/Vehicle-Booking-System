package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.model.Designation;
import com.jxg.isn_backend.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/designations")
public class DesignationController {

    @Autowired
    private DesignationService designationService;


    @PostMapping
    public ResponseEntity<Designation> addDesignation(@RequestBody Designation designation) {
        Designation newDesignation = designationService.createDesignation(designation.getName());
        return ResponseEntity.ok(newDesignation);
    }

    @GetMapping
    public ResponseEntity<List<Designation>> getAllDesignations() {
        List<Designation> designations = designationService.getAllDesignations();
        return ResponseEntity.ok(designations);
    }

    // Get a designation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Designation> getDesignationById(@PathVariable Integer id) {
        Optional<Designation> designation = designationService.getDesignationById(id);
        if (designation.isPresent()) {
            return ResponseEntity.ok(designation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Integer id) {
        Optional<Designation> designation = designationService.getDesignationById(id);
        if (designation.isPresent()) {
            designationService.deleteDesignation(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadImageForDesignation(@PathVariable Integer id,
                                                            @RequestParam("file") MultipartFile file) {
        return designationService.uploadImageForDesignation(id, file);
    }

}


