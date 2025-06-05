package com.backend.controller;

import com.backend.service.AWSS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationResponse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/s3")
@CrossOrigin(origins = "*")
public class AWSS3Controller {

    private static final Logger logger = LoggerFactory.getLogger(AWSS3Controller.class);
    private final AWSS3Service awss3Service;
    
    @Autowired
    private S3Client s3Client;

    public AWSS3Controller(AWSS3Service awss3Service) {
        this.awss3Service = awss3Service;
    }

    /**
     * Download PDF from S3
     * GET /s3/download/{bucketName}?path={s3Path}
     */
    @GetMapping("/download/{bucketName}")
    public ResponseEntity<Resource> downloadPdf(
            @PathVariable String bucketName,
            @RequestParam String path) {

        try {
            logger.info("Request to download PDF - bucket: {}, path: {}", bucketName, path);

            Resource resource = awss3Service.getPdfResourceFromS3(bucketName, path);

            // Extract filename from path
            String filename = path.substring(path.lastIndexOf('/') + 1);
            if (!filename.endsWith(".pdf")) {
                filename += ".pdf";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments for download request", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error downloading PDF from S3", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Upload PDF to S3
     * POST /s3/upload/{bucketName}?path={s3Path}
     */
    @PostMapping("/upload/{bucketName}")
    public ResponseEntity<Map<String, String>> uploadPdf(
            @PathVariable String bucketName,
            @RequestParam String path,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            logger.info("Request to upload PDF - bucket: {}, path: {}, filename: {}",
                    bucketName, path, file.getOriginalFilename());

            // Validate file
            if (file.isEmpty()) {
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            if (!file.getContentType().equals("application/pdf")) {
                response.put("error", "File must be a PDF");
                return ResponseEntity.badRequest().body(response);
            }

            // Convert MultipartFile to Resource
            Resource resource = file.getResource();

            awss3Service.uploadPdfToS3(bucketName, path, resource);

            response.put("message", "PDF uploaded successfully");
            response.put("bucket", bucketName);
            response.put("path", path);
            response.put("filename", file.getOriginalFilename());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments for upload request", e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error uploading PDF to S3", e);
            response.put("error", "Failed to upload PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete PDF from S3
     * DELETE /s3/delete/{bucketName}?path={s3Path}
     */
    @DeleteMapping("/delete/{bucketName}")
    public ResponseEntity<Map<String, String>> deletePdf(
            @PathVariable String bucketName,
            @RequestParam String path) {

        Map<String, String> response = new HashMap<>();

        try {
            logger.info("Request to delete PDF - bucket: {}, path: {}", bucketName, path);

            awss3Service.deletePdfFromS3(bucketName, path);

            response.put("message", "PDF deleted successfully");
            response.put("bucket", bucketName);
            response.put("path", path);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments for delete request", e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error deleting PDF from S3", e);
            response.put("error", "Failed to delete PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Check if object exists in S3
     * GET /s3/exists/{bucketName}?path={s3Path}
     */
    @GetMapping("/exists/{bucketName}")
    public ResponseEntity<Map<String, Object>> checkObjectExists(
            @PathVariable String bucketName,
            @RequestParam String path) {

        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("Request to check object existence - bucket: {}, path: {}", bucketName, path);

            boolean exists = awss3Service.doesObjectExist(bucketName, path);

            response.put("exists", exists);
            response.put("bucket", bucketName);
            response.put("path", path);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid arguments for existence check", e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error checking object existence in S3", e);
            response.put("error", "Failed to check object existence: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get list of all PDF files from S3 bucket
     * GET /s3/list/{bucketName}
     */
    @GetMapping("/list/{bucketName}")
    public ResponseEntity<Map<String, Object>> listPdfFiles(@PathVariable String bucketName) {

        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("Request to list PDF files - bucket: {}", bucketName);

            List<File> pdfFiles = awss3Service.getAllPdfFilesFromS3(bucketName);

            response.put("bucket", bucketName);
            response.put("count", pdfFiles.size());
            response.put("message", "PDF files retrieved successfully");

            // Note: Returning file paths instead of File objects for JSON serialization
            List<String> filePaths = pdfFiles.stream()
                    .map(File::getAbsolutePath)
                    .toList();
            response.put("files", filePaths);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid bucket name for list request", e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error listing PDF files from S3", e);
            response.put("error", "Failed to list PDF files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get PDF files with their S3 keys
     * GET /s3/list-with-keys/{bucketName}
     */
    @GetMapping("/list-with-keys/{bucketName}")
    public ResponseEntity<Map<String, Object>> listPdfFilesWithKeys(@PathVariable String bucketName) {

        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("Request to list PDF files with keys - bucket: {}", bucketName);

            Map<String, File> pdfFilesMap = awss3Service.getPdfFilesWithKeys(bucketName);

            response.put("bucket", bucketName);
            response.put("count", pdfFilesMap.size());
            response.put("message", "PDF files with keys retrieved successfully");

            // Convert File objects to file paths for JSON serialization
            Map<String, String> filePathsMap = new HashMap<>();
            pdfFilesMap.forEach((key, file) -> filePathsMap.put(key, file.getAbsolutePath()));
            response.put("files", filePathsMap);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid bucket name for list-with-keys request", e);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Error listing PDF files with keys from S3", e);
            response.put("error", "Failed to list PDF files with keys: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Health check endpoint
     * GET /s3/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "AWS S3 Service");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    /**
     * Get bucket location/region for debugging
     * GET /s3/bucket-location/{bucketName}
     */
    @GetMapping("/bucket-location/{bucketName}")
    public ResponseEntity<Map<String, String>> getBucketLocation(@PathVariable String bucketName) {
        Map<String, String> response = new HashMap<>();
        try {
            logger.info("Checking bucket location for: {}", bucketName);
            
            GetBucketLocationRequest request = GetBucketLocationRequest.builder()
                    .bucket(bucketName)
                    .build();
            
            GetBucketLocationResponse locationResponse = s3Client.getBucketLocation(request);
            String region = locationResponse.locationConstraintAsString();
            
            response.put("bucket", bucketName);
            response.put("region", region);
            response.put("status", "success");
            
            logger.info("Bucket {} is in region: {}", bucketName, region);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error checking bucket location for {}: {}", bucketName, e.getMessage(), e);
            response.put("bucket", bucketName);
            response.put("error", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}