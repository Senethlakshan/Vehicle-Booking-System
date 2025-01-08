package com.jxg.isn_backend.dto.response;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private Long createdBy;
    private Long lastModifiedBy;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;

}