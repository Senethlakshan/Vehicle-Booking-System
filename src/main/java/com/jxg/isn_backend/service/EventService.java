package com.jxg.isn_backend.service;

import com.jxg.isn_backend.dto.response.CreateEventResponseDTO;
import com.jxg.isn_backend.dto.response.EventResponseDTO;

import java.io.IOException;
import java.util.Set;

public interface EventService {
    CreateEventResponseDTO createEvent(CreateEventResponseDTO requestDTO) throws IOException;

    Set<EventResponseDTO> getAllEvents();
}
