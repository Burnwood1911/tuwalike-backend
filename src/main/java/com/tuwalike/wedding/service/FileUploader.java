package com.tuwalike.wedding.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.time.Duration;

import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileUploader {

    public String upload(byte[] pngBytes, String filename) {
        try {
            // Create a MinioClient object using your Minio server's URL and your access
            // credentials.
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("https://minio.alexrossi.xyz")
                    .credentials("Ppw6g3wGeAgPt1QEMeJs", "SEbMikbhURym5eJ5FFSz1Pa87CQg5t94mZT29Vec")
                    .build();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pngBytes);

            minioClient.putObject(
                    PutObjectArgs.builder().bucket("tualike").object(filename)
                            .stream(byteArrayInputStream, -1, 10485760) // 10MB part size
                            .contentType("image/png") // Adjust content type based on your file
                            .build());

            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket("tualike")
                    .object(filename)
                    .build();
            String presignedUrl = minioClient.getPresignedObjectUrl(args);

            int questionMarkIndex = presignedUrl.indexOf("?");
            String modifiedUrl = presignedUrl.substring(0, questionMarkIndex);

            log.info("Presigned: {}", modifiedUrl);

            return modifiedUrl;
        } catch (Exception e) {
            System.err.println("Error occurred: " + e);
            e.printStackTrace();
            return "";
        }
    }
}
