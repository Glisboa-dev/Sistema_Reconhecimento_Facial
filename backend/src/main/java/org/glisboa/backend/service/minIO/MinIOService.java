package org.glisboa.backend.service.minIO;

import org.springframework.web.multipart.MultipartFile;

public interface MinIOService {

    String uploadFile(MultipartFile file);
    void deleteFile(String objectName);
}
