package com.itmo.is.lz.pipivo.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioService(MinioClient minioClient, @Value("${minio.bucket.name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("MinIO: Бакет " + bucketName + " создан.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке/создании бакета в MinIO", e);
        }
    }

    public void uploadFile(String fileName, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build());
            System.out.println("MinIO: Файл " + fileName + " загружен.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке файла в MinIO", e);
        }
    }

    public String generateUrl(String fileName) {
        try {
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .method(Method.GET)
                    .expiry(5, TimeUnit.DAYS)
                    .build());
            System.out.println(url);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации ссылки MinIO", e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            System.out.println("MinIO: Файл " + fileName + " удалён.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла из MinIO", e);
        }
    }
}
