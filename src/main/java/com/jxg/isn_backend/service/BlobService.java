package com.jxg.isn_backend.service;

import com.jxg.isn_backend.model.FileBlob;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BlobService {

    FileBlob saveBlobToLocal(String directory, MultipartFile file) throws IOException;

    byte[] getBlobFromLocal(String directory, String fileName) throws IOException;

    void deleteLocalBlob(String directory, String fileName) throws IOException;
}
