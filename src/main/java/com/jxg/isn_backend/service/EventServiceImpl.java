package com.jxg.isn_backend.service;


import com.jxg.isn_backend.dto.request.CreateEventRequestDTO;
import com.jxg.isn_backend.dto.response.CreateEventResponseDTO;
import com.jxg.isn_backend.mapper.BlobMapper;
import com.jxg.isn_backend.mapper.UserMapper;
import com.jxg.isn_backend.model.Event;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class EventServiceImpl {

    @Value("${spring.app.localBlobDirectory}")
    private String FILE_DIRECTORY;
    private final EventRepository eventRepository;
    private final BlobService blobService;

    public EventServiceImpl(EventRepository eventRepository, BlobService blobService) {
        this.eventRepository = eventRepository;
        this.blobService = blobService;
    }

    public List<CreateEventResponseDTO> findAll() {
        var eventList = eventRepository.findAll();
        var eventResponseDTOList = new ArrayList<CreateEventResponseDTO>();
        for (var event : eventList) {
            eventResponseDTOList.add(new CreateEventResponseDTO(
                    event.getId(),
                    event.getDescription(),
                    event.getTitle(),
                    event.getDepartments(),
                    BlobMapper.INSTANCE.toDto(event.getImageBlob()),
                    UserMapper.INSTANCE.toUserMinDTO(event.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(event.getLastModifiedBy()),
                    event.getCreatedDatetime(),
                    event.getLastModifiedDatetime()
            ));
        }
        return eventResponseDTOList;

    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public CreateEventResponseDTO createEvent(CreateEventRequestDTO requestDTO, MultipartFile file) throws IOException {
        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);
        var newEvent= new Event();

        newEvent.setImageBlob(fileBlob);
        newEvent.setTitle(requestDTO.title() != null ? requestDTO.title() : null);
        newEvent.setDescription(requestDTO.description() != null ? requestDTO.description() : null);


        var savedEvent = this.eventRepository.save(newEvent);
        return new CreateEventResponseDTO(
                savedEvent.getId(),
                savedEvent.getDescription(),
                savedEvent.getTitle(),
                savedEvent.getDepartments(),
                BlobMapper.INSTANCE.toDto(savedEvent.getImageBlob()),
                UserMapper.INSTANCE.toUserMinDTO(savedEvent.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(savedEvent.getLastModifiedBy()),
                savedEvent.getCreatedDatetime(),
                savedEvent.getLastModifiedDatetime()
        );
    }


    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
//


}

