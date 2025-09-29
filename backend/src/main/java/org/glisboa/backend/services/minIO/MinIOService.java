package org.glisboa.backend.services.minIO;

import org.springframework.web.multipart.MultipartFile;

public interface MinIOService {

    String uploadFile(MultipartFile file);
    void deleteFile(String objectName);
}
