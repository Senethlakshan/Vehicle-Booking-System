package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.dto.request.CreateEventRequestDTO;
import com.jxg.isn_backend.dto.response.CreateEventResponseDTO;
import com.jxg.isn_backend.service.EventServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/events")
public class EventController {

    private final EventServiceImpl eventService;

    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<CreateEventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }
    @PostMapping
    public ResponseEntity<CreateEventResponseDTO> addEvent(@RequestPart(name = "data") CreateEventRequestDTO requestDTO, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(this.eventService.createEvent(requestDTO, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.findById(id).isPresent()) {
            eventService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
