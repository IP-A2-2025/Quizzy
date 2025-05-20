package com.backend;

import com.backend.service.AWSS3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AWSS3ServiceTest {

    @Mock private S3Client s3Client;

    @InjectMocks private AWSS3Service s3Service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // --- getPdfResourceFromS3 ---
    @Test
    void getPdfResourceFromS3_validPdf_returnsResource() throws IOException {
        String bucket = "bucket", key = "file.pdf";
        byte[] content = "pdf-content".getBytes();

        GetObjectResponse response = GetObjectResponse.builder()
                .contentType("application/pdf")
                .contentLength((long) content.length)
                .build();

        ResponseInputStream<GetObjectResponse> stream =
                new ResponseInputStream<>(response, AbortableInputStream.create(new ByteArrayInputStream(content)));

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(stream);

        Resource result = s3Service.getPdfResourceFromS3(bucket, key);

        assertNotNull(result);
        assertTrue(result instanceof InputStreamResource);
    }

    @Test
    void getPdfResourceFromS3_invalidContentType_throwsException() {
        String bucket = "bucket", key = "file.txt";
        GetObjectResponse response = GetObjectResponse.builder().contentType("text/plain").build();

        ResponseInputStream<GetObjectResponse> stream =
                new ResponseInputStream<>(response, AbortableInputStream.create(new ByteArrayInputStream("x".getBytes())));

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(stream);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                s3Service.getPdfResourceFromS3(bucket, key));
        assertTrue(ex.getMessage().contains("not a PDF"));
    }

    @Test
    void getPdfResourceFromS3_nullStream_throwsException() {
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () ->
                s3Service.getPdfResourceFromS3("bucket", "file.pdf"));
    }

    @Test
    void getPdfResourceFromS3_s3Exception_throwsRuntime() {
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().message("S3 failure").build());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                s3Service.getPdfResourceFromS3("bucket", "file.pdf"));

        assertTrue(ex.getMessage().contains("S3 failure"));
    }

    @Test
    void getPdfResourceFromS3_invalidArgs_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.getPdfResourceFromS3(null, "key"));
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.getPdfResourceFromS3("bucket", " "));
    }

    // --- uploadPdfToS3 ---
    @Test
    void uploadPdfToS3_validResource_uploadsSuccessfully() throws IOException {
        byte[] data = "test".getBytes();

        ByteArrayResource resource = new ByteArrayResource(data) {
            @Override public String getFilename() {
                return "test.pdf";
            }
        };

        when(s3Client.putObject((PutObjectRequest) any(), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().eTag("123").build());

        s3Service.uploadPdfToS3("bucket", "file.pdf", resource);

        verify(s3Client).putObject((PutObjectRequest) any(), any(RequestBody.class));
    }

    @Test
    void uploadPdfToS3_invalidFilename_throwsException() {
        Resource resource = mock(Resource.class);
        when(resource.getFilename()).thenReturn("invalid.txt");
        when(resource.exists()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                s3Service.uploadPdfToS3("bucket", "file.txt", resource));
    }

    @Test
    void uploadPdfToS3_nullOrMissingResource_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.uploadPdfToS3("bucket", "file", null));

        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                s3Service.uploadPdfToS3("bucket", "file", resource));
    }

    @Test
    void uploadPdfToS3_ioException_throwsRuntime() throws IOException {
        Resource resource = mock(Resource.class);
        when(resource.getFilename()).thenReturn("test.pdf");
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () ->
                s3Service.uploadPdfToS3("bucket", "file", resource));
    }

    @Test
    void uploadPdfToS3_s3Exception_throwsRuntime() throws IOException {
        Resource resource = mock(Resource.class);
        when(resource.getFilename()).thenReturn("test.pdf");
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("abc".getBytes()));
        when(resource.contentLength()).thenReturn(3L);

        when(s3Client.putObject((PutObjectRequest) any(), (RequestBody) any())).thenThrow(S3Exception.builder().message("S3 error").build());

        assertThrows(RuntimeException.class, () ->
                s3Service.uploadPdfToS3("bucket", "file", resource));
    }

    @Test
    void uploadPdfToS3_invalidArgs_throwsException() {
        ByteArrayResource res = new ByteArrayResource("x".getBytes()) {
            @Override public String getFilename() { return "x.pdf"; }
        };

        assertThrows(IllegalArgumentException.class, () ->
                s3Service.uploadPdfToS3(null, "key", res));
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.uploadPdfToS3("bucket", " ", res));
    }

    // --- doesObjectExist ---
    @Test
    void doesObjectExist_exists_returnsTrue() {
        when(s3Client.headObject((HeadObjectRequest) any())).thenReturn(HeadObjectResponse.builder().build());
        assertTrue(s3Service.doesObjectExist("bucket", "key"));
    }

    @Test
    void doesObjectExist_404_returnsFalse() {
        S3Exception ex = (S3Exception) S3Exception.builder().statusCode(404).build();
        when(s3Client.headObject((HeadObjectRequest) any())).thenThrow(ex);

        assertFalse(s3Service.doesObjectExist("bucket", "key"));
    }

    @Test
    void doesObjectExist_otherS3Exception_throwsRuntime() {
        S3Exception ex = (S3Exception) S3Exception.builder().statusCode(500).message("Internal").build();
        when(s3Client.headObject((HeadObjectRequest) any())).thenThrow(ex);

        RuntimeException err = assertThrows(RuntimeException.class, () ->
                s3Service.doesObjectExist("bucket", "key"));
        assertTrue(err.getMessage().contains("Error checking object"));
    }

    @Test
    void doesObjectExist_invalidArgs_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.doesObjectExist(null, "key"));
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.doesObjectExist("bucket", ""));
    }

    // --- getAllCourseFilesFromS3 ---
    @Test
    void getAllCourseFilesFromS3_validPdf_downloadsToTempFile() throws IOException {
        S3Object obj = S3Object.builder().key("courses/file1.pdf").build();
        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(Collections.singletonList(obj)).build();

        byte[] pdf = "PDF".getBytes();
        GetObjectResponse objResponse = GetObjectResponse.builder().contentType("application/pdf").build();
        ResponseInputStream<GetObjectResponse> stream =
                new ResponseInputStream<>(objResponse, AbortableInputStream.create(new ByteArrayInputStream(pdf)));

        when(s3Client.listObjectsV2((ListObjectsV2Request) any())).thenReturn(listResponse);
        when(s3Client.getObject((GetObjectRequest) any())).thenReturn(stream);

        List<File> files = s3Service.getAllPdfFilesFromS3("bucket");

        assertEquals(1, files.size());
        assertTrue(files.get(0).getName().endsWith(".pdf"));
        assertTrue(files.get(0).exists());
    }

    @Test
    void getAllCourseFilesFromS3_skipsNonPdfFiles() {
        S3Object obj = S3Object.builder().key("courses/file.txt").build();
        ListObjectsV2Response listResponse = ListObjectsV2Response.builder()
                .contents(Collections.singletonList(obj)).build();

        when(s3Client.listObjectsV2((ListObjectsV2Request) any())).thenReturn(listResponse);

        List<File> files = s3Service.getAllPdfFilesFromS3("bucket");
        assertTrue(files.isEmpty());
    }

    @Test
    void getAllCourseFilesFromS3_exceptionThrown_throwsRuntime() {
        when(s3Client.listObjectsV2((ListObjectsV2Request) any()))
                .thenThrow(S3Exception.builder().message("fail").build());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                s3Service.getAllPdfFilesFromS3("bucket"));
        assertTrue(ex.getMessage().contains("Failed to fetch"));
    }

    @Test
    void getAllCourseFilesFromS3_invalidArgs_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                s3Service.getAllPdfFilesFromS3(""));
    }
}
