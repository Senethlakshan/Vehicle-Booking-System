package com.jxg.isn_backend.service;

import com.jxg.isn_backend.model.Designation;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DesignationService {

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
}
