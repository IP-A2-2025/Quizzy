package com.backend.controller;

import com.backend.model.Flashcard;
import com.backend.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.utils.FlashcardImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Flashcard")
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    private FlashcardImport flashcardImport;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> getFlashcardById(@PathVariable Long id) {
        return flashcardService.getFlashcardById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard) {
        Flashcard created = flashcardService.createFlashcard(flashcard);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flashcard> updateFlashcard(@PathVariable Long id, @RequestBody Flashcard flashcard) {
        Flashcard updated = flashcardService.updateFlashcard(id, flashcard);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Flashcard>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardService.getByUserId(userId));
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByMaterialId(@PathVariable Long materialId) {
        return ResponseEntity.ok(flashcardService.getByMaterialId(materialId));
    }

    @GetMapping("/due")
    public ResponseEntity<List<Flashcard>> getDueFlashcards(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date,
            @RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(flashcardService.getDueFlashcards(date, userId));
    }

    @GetMapping("/page/{pageIndex}")
    public ResponseEntity<List<Flashcard>> getByPageIndex(@PathVariable Integer pageIndex) {
        return ResponseEntity.ok(flashcardService.getByPageIndex(pageIndex));
    }

    @GetMapping("/page/{pageIndex}/user/{userId}")
    public ResponseEntity<List<Flashcard>> getByPageIndexAndUserId(
            @PathVariable Integer pageIndex,
            @PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardService.getByPageIndexAndUserId(pageIndex, userId));
    }

    @GetMapping("/page/{pageIndex}/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByPageIndexAndMaterialId(
            @PathVariable Integer pageIndex,
            @PathVariable Long materialId) {
        return ResponseEntity.ok(flashcardService.getByPageIndexAndMaterialId(pageIndex, materialId));
    }

    @PostMapping("/generate-course")
    public ResponseEntity<Map<String, Object>> generateFlashcardsForCourse(@RequestParam String course) {
        try {
            String projectPath = new File("").getAbsolutePath();
            String folderPath = projectPath + "/courses/" + course;

            int importedCount = flashcardImport.importFlashcardsFromDirectory(folderPath, 1);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("importedCount", importedCount);
            response.put("course", course);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("course", course);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/generate-all")
    public ResponseEntity<Map<String, Object>> generateAllFlashcards() {
        try {
            String projectPath = new File("").getAbsolutePath();
            String coursesPath = projectPath + "/courses";

            File coursesDir = new File(coursesPath);
            if (!coursesDir.exists() || !coursesDir.isDirectory()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Courses directory not found: " + coursesPath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            List<Map<String, Object>> courseResults = new ArrayList<>();
            int totalImported = 0;
            int successfulCourses = 0;
            int failedCourses = 0;

            File[] courseDirectories = coursesDir.listFiles(File::isDirectory);
            if (courseDirectories == null || courseDirectories.length == 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "No course directories found in: " + coursesPath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            for (File courseDir : courseDirectories) {
                String courseName = courseDir.getName();
                Map<String, Object> courseResult = new HashMap<>();
                courseResult.put("course", courseName);

                try {
                    int courseImported = flashcardImport.importFlashcardsFromDirectory(courseDir.getAbsolutePath(), 1);
                    courseResult.put("status", "success");
                    courseResult.put("importedCount", courseImported);
                    totalImported += courseImported;
                    successfulCourses++;

                    System.out.println("Successfully imported " + courseImported + " flashcards from course: " + courseName);

                } catch (Exception e) {
                    courseResult.put("status", "error");
                    courseResult.put("message", e.getMessage());
                    courseResult.put("importedCount", 0);
                    failedCourses++;

                    System.err.println("Failed to import flashcards from course " + courseName + ": " + e.getMessage());
                }

                courseResults.add(courseResult);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "completed");
            response.put("totalImported", totalImported);
            response.put("totalCourses", courseDirectories.length);
            response.put("successfulCourses", successfulCourses);
            response.put("failedCourses", failedCourses);
            response.put("courseResults", courseResults);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getAvailableCourses() {
        try {
            String projectPath = new File("").getAbsolutePath();
            String coursesPath = projectPath + "/courses";

            File coursesDir = new File(coursesPath);
            if (!coursesDir.exists() || !coursesDir.isDirectory()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Courses directory not found: " + coursesPath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            List<Map<String, Object>> courses = new ArrayList<>();
            File[] courseDirectories = coursesDir.listFiles(File::isDirectory);

            if (courseDirectories != null) {
                for (File courseDir : courseDirectories) {
                    Map<String, Object> courseInfo = new HashMap<>();
                    courseInfo.put("name", courseDir.getName());
                    courseInfo.put("path", courseDir.getAbsolutePath());

                    // Count flashcard files in this course
                    File[] flashcardFiles = courseDir.listFiles((dir, name) -> name.endsWith("_flashcards.txt"));
                    courseInfo.put("flashcardFiles", flashcardFiles != null ? flashcardFiles.length : 0);

                    courses.add(courseInfo);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("totalCourses", courses.size());
            response.put("courses", courses);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}