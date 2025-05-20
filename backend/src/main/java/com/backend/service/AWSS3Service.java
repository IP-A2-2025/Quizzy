package com.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class AWSS3Service {

    private static final Logger logger = LoggerFactory.getLogger(AWSS3Service.class);
    private final S3Client s3Client;

    public AWSS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Resource getPdfResourceFromS3(String bucketName, String s3Path) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (s3Path == null || s3Path.trim().isEmpty()) {
            throw new IllegalArgumentException("S3 path cannot be null or empty");
        }

        try {
            logger.info("Attempting to retrieve file from S3 - bucket: '{}', path: '{}'", bucketName, s3Path);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            logger.debug("Sending GetObject request to S3");
            ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);

            if (responseInputStream == null) {
                logger.error("Null response input stream for bucket: '{}', path: '{}'", bucketName, s3Path);
                throw new NullPointerException("S3 returned a null response input stream");
            }

            GetObjectResponse response = responseInputStream.response();
            logger.debug("Received S3 response - Content Length: {}, Content Type: {}", 
                    response.contentLength(), response.contentType());

            if (response == null || !"application/pdf".equalsIgnoreCase(response.contentType())) {
                String contentType = response != null ? response.contentType() : "null";
                logger.error("Invalid content type for S3 object - Expected: application/pdf, Got: {}", contentType);
                throw new RuntimeException("The downloaded object is not a PDF file. Content type: " + contentType);
            }

            logger.info("Successfully retrieved file from S3 - Content Length: {}, Content Type: {}",
                    response.contentLength(), response.contentType());

            return new InputStreamResource(responseInputStream);

        } catch (S3Exception e) {
            logger.error("S3 error occurred - Status Code: {}, Error Message: {}, Bucket: '{}', Path: '{}'", 
                    e.statusCode(),
                    e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage(),
                    bucketName,
                    s3Path,
                    e);
            String errorMsg = "Error downloading PDF from S3: " +
                    (e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage());
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void uploadPdfToS3(String bucketName, String s3Path, Resource pdfResource) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (s3Path == null || s3Path.trim().isEmpty()) {
            throw new IllegalArgumentException("S3 path cannot be null or empty");
        }
        if (pdfResource == null || !pdfResource.exists()) {
            throw new IllegalArgumentException("PDF resource cannot be null and must exist");
        }

        try (InputStream inputStream = pdfResource.getInputStream()) {
            if (!pdfResource.getFilename().endsWith(".pdf")) {
                throw new IllegalArgumentException("The provided resource must be a PDF");
            }

            logger.debug("Attempting to upload file to S3 - bucketName: {}, s3Path: {}", bucketName, s3Path);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .contentType("application/pdf")
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, pdfResource.contentLength())
            );

            logger.info("Successfully uploaded PDF to S3. ETag: {}, VersionId: {}", response.eTag(), response.versionId());

        } catch (IOException e) {
            logger.error("Error processing PDF resource for upload", e);
            throw new RuntimeException("Error reading PDF resource: " + e.getMessage(), e);
        } catch (S3Exception e) {
            logger.error("Error uploading PDF to S3 - bucketName: {}, s3Path: {}", bucketName, s3Path, e);
            String errorMessage = e.awsErrorDetails() != null
                    ? e.awsErrorDetails().errorMessage()
                    : e.getMessage();
            throw new RuntimeException("Error uploading PDF to S3: " + errorMessage, e);
        }
    }

    public boolean doesObjectExist(String bucketName, String s3Path) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (s3Path == null || s3Path.trim().isEmpty()) {
            throw new IllegalArgumentException("S3 path cannot be null or empty");
        }

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            if (e instanceof NoSuchKeyException || (e.statusCode() == 404)) {
                logger.debug("S3 object does not exist - bucket: {}, path: {}", bucketName, s3Path);
                return false;
            }
            logger.error(
                "S3 error (status code: {}, AWS error message: {}). Bucket: {}, path: {}",
                e.statusCode(),
                e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage(),
                bucketName,
                s3Path
            );

            throw new RuntimeException("Error checking object existence in S3: " + e.getMessage(), e);
        }
    }

    public List<File> getAllPdfFilesFromS3(String bucketName) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }

        try {
            List<File> pdfFiles = new ArrayList<>();
            String continuationToken = null;

            do {
                ListObjectsV2Request.Builder listRequestBuilder = ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .maxKeys(1000);

                if (continuationToken != null) {
                    listRequestBuilder.continuationToken(continuationToken);
                }

                ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequestBuilder.build());

                for (S3Object s3Object : listResponse.contents()) {
                    String key = s3Object.key();

                    if (!key.toLowerCase().endsWith(".pdf")) {
                        continue;
                    }

                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(getObjectRequest)) {
                        File tempFile = Files.createTempFile("s3-pdf-", ".pdf").toFile();
                        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                            inputStream.transferTo(outputStream);
                        }

                        pdfFiles.add(tempFile);
                        logger.info("Downloaded PDF from S3: {}", tempFile.getAbsolutePath());
                    }
                }

                continuationToken = listResponse.nextContinuationToken();

            } while (continuationToken != null);

            return pdfFiles;

        } catch (IOException | S3Exception e) {
            logger.error("Error fetching PDF files from S3", e);
            throw new RuntimeException("Failed to fetch PDF files from S3: " + e.getMessage(), e);
        }
    }

}