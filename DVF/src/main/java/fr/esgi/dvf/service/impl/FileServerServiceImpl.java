package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.service.FileServerService;
import io.minio.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class FileServerServiceImpl implements FileServerService {
    private final Logger logger = LogManager.getLogger(FileServerServiceImpl.class);
    @Value("${bucketName}")
    private String bucketName;

    @Value("${bucketUrl}")
    private String bucketUrl;

    @Value("${bucketPolicy}")
    private String bucketPolicy;

    @Value("${fileServerAccessKey}")
    private String fileServerAccessKey;

    @Value("${fileServerSecretKey}")
    private String fileServerSecretKey;

    private MinioClient minioClient;

    @Bean
    public void initFileServer(){
        minioClient = MinioClient.builder().endpoint(bucketUrl).credentials(fileServerAccessKey, fileServerSecretKey).build();
    }

    @Override
    public String enregistrerFichier(File file){
        String finalPath = Paths.get(bucketName, file.getName()).toString();
        try{
            boolean isBucketExistant = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isBucketExistant) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(bucketPolicy).build());
            }
            minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(file.getName()).filename(file.getAbsolutePath()).build()).etag();
        }catch (Exception ex){
            logger.error("Une erreur est survenue lors de l'enregistrement du fichier");
            logger.error(ex.getMessage());
        }
        return finalPath;
    }
}
