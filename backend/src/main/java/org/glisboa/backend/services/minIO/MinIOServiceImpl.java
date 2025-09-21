package org.glisboa.backend.services.minIO;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MinIOServiceImpl implements MinIOService{
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    
}
