package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.model.FileBlob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlobRepository extends JpaRepository<FileBlob, Long> {
}
