package org.glisboa.backend.services.minIO;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class MinIOServiceImpl implements MinIOService {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private final String bucketName;


    @Override
    public String uploadFile(MultipartFile file, String studentId){
        String objectName = generateObjectName(file.getOriginalFilename(), studentId);
        try(InputStream is = file.getInputStream()){
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .contentType("image/jpeg")
                            .stream(is, file.getSize(), -1)
                            .build()
            );
        }catch (Exception e){
            throw new RuntimeException("Erro ao fazer upload do arquivo");
        }

        return objectName;
    }

    @Override
    public void deleteFile(String objectName) {
       try{
           minioClient.removeObject(
                   RemoveObjectArgs.builder()
                           .bucket(bucketName)
                           .object(objectName)
                           .build()
           );
       }catch (Exception e){
              throw new RuntimeException("Erro ao deletar o arquivo");
       }
    }


    private String generateObjectName(String fileName, String studentId) {
        return fileName + "_" + studentId + System.currentTimeMillis();
    }

}
