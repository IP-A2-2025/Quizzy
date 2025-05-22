package com.backend.controller;


import com.backend.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.backend.dto.MaterialDTO;
import org.springframework.beans.factory.annotation.Value;
import com.backend.model.Material;
import com.backend.service.AWSS3Service;
import com.backend.service.CourseService;
import com.backend.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/Material")
public class MaterialController {


    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    private final CourseService courseService;
    private final MaterialService materialService;
    private final AWSS3Service awsS3Service;

    @Autowired
    public MaterialController(MaterialService materialService, CourseService courseService, AWSS3Service awsS3Service) {
        this.materialService = materialService;
        this.courseService = courseService;
        this.awsS3Service = awsS3Service;
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAll() {
        List<Material> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        return ResponseEntity.ok(material);
    }

    @PostMapping
    public ResponseEntity<Material> create(@RequestBody MaterialDTO material) {
        Material created = materialService.createMaterial(material);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> update(@PathVariable Long id, @RequestBody Material material) {
        Material updated = materialService.updateMaterial(id, material);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Material>> getByCourse(@PathVariable Long courseId) {
        List<Material> materials = materialService.findByCourseId(courseId);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Material>> searchByName(@RequestParam String name) {
        List<Material> materials = materialService.findByNameContaining(name);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/search/path")
    public ResponseEntity<List<Material>> searchByPath(@RequestParam String path) {
        List<Material> materials = materialService.findByPathContaining(path);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Material>> getByProfessor(@PathVariable Integer professorId) {
        List<Material> materials = materialService.findByProfessorId(professorId);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/path/cursuri/{courseTitle}/{courseName}")
    public ResponseEntity<Resource> getPDF(@PathVariable String courseTitle, @PathVariable String courseName) {
        logger.info("Received request to get PDF for course: '{}' with name: {}", courseTitle, courseName);
        try {
            Course course = courseService.findByTitle(courseTitle);
            if(course == null) {
                logger.error("Course '{}' not found", courseTitle);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Optional<Material> materialOptional = course.getMaterials().stream().filter( m  ->m.getName().equals(courseName)).findFirst();
            Material material = null;
            if(materialOptional.isPresent()) {
                material = materialOptional.get();
            } else{
                logger.error("Material couldn't be found by name {}, for {}!",courseName,courseTitle);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String s3Path = material.getPath();
            if (s3Path == null || s3Path.isEmpty()) {
                logger.error("Material with name '{}' for course '{}' has an invalid S3 path", courseName, courseTitle);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            logger.debug("Fetching PDF from S3 bucket '{}' with path '{}'", bucketName, s3Path);
            Resource pdfResource = awsS3Service.getPdfResourceFromS3(bucketName, s3Path);
            if (pdfResource == null) {
                logger.warn("PDF file not found in S3 for path '{}'", s3Path);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            logger.info("Successfully retrieved PDF for course '{}' with name '{}'", courseTitle, courseName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);

        } catch (Exception e) {
            logger.error("Unexpected error while retrieving PDF for course '{}' with name '{}': {}", courseTitle, courseName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}