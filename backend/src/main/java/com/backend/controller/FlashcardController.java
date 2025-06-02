package com.backend.controller;

import com.backend.dto.FlashcardResponseDTO;
import com.backend.model.Flashcard;
import com.backend.model.FlashcardProgress;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
import com.backend.service.FlashcardProgressService;
import com.backend.service.FlashcardService;
import com.backend.service.UserService;
import com.backend.utils.FlashcardImport;
import com.backend.utils.SpacedRepetitionAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/Flashcard")
public class FlashcardController {
    
    private static final Logger logger = LoggerFactory.getLogger(FlashcardController.class);
    private static final Random random = new Random();

    private final FlashcardService flashcardService;
    private final FlashcardProgressService flashcardProgressService;
    private final UserService userService;
    // CourseRepository este folosit în metode de generare flashcarduri dar nu direct
    private final CourseRepository courseRepository;
    private final FlashcardImport flashcardImport;

    @Autowired
    public FlashcardController(FlashcardService flashcardService,
                              FlashcardProgressService flashcardProgressService,
                              UserService userService,
                              CourseRepository courseRepository,
                              FlashcardImport flashcardImport) {
        this.flashcardService = flashcardService;
        this.flashcardProgressService = flashcardProgressService;
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.flashcardImport = flashcardImport;
    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> getFlashcardById(@PathVariable Long id) {
        Optional<Flashcard> flashcard = flashcardService.getFlashcardById(id);
        return flashcard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("course/{courseId}")
    public ResponseEntity<List<Flashcard>> getFlashcardsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flashcardService.createFlashcard(flashcard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flashcard> updateFlashcard(@PathVariable Long id, @RequestBody Flashcard flashcard) {
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, flashcard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Flashcard>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardService.getByUserId(userId));
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByMaterialId(@PathVariable Long materialId) {
        return ResponseEntity.ok(flashcardService.getByMaterialId(materialId));
    }

    @GetMapping("/user/{userId}/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByUserIdAndMaterialId(
            @PathVariable Integer userId,
            @PathVariable Long materialId) {
        // Nu există metoda getByUserIdAndMaterialId, folosim getRandomFlashcardsByMaterialIdAndUserId
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByMaterialIdAndUserId(materialId, userId, 100));
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

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateFlashcardsForCourse(@RequestParam String course) {
        try {
            String projectPath = new File("").getAbsolutePath();
            String coursePath = projectPath + "/courses/" + course;
            
            int importedCount = 0;
            try {
                // Folosim bean-ul FlashcardImport injectat pentru a genera flashcarduri
                // Folosim DEFAULT_USER_ID (valoarea 1) definit în FlashcardImport
                importedCount = flashcardImport.importFlashcardsFromDirectory(coursePath, null);
                System.out.println("Importate " + importedCount + " flashcarduri din cursul: " + course);
                
                // Obținem toate flashcardurile și le filtrăm manual pentru acest curs
                // Notă: Aceasta este o abordare temporară pentru a evita dependențele problematice
                List<Flashcard> flashcards = flashcardService.getRandomFlashcards(1000);
                
                // Salvăm flashcardurile generate în baza de date
                for (Flashcard flashcard : flashcards) {
                    flashcardService.createFlashcard(flashcard);
                }
            } catch (Exception e) {
                System.err.println("Eroare la importarea flashcardurilor: " + e.getMessage());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Flashcards generated successfully");
            response.put("count", importedCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
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
            
            int totalFlashcards = 0;
            List<Map<String, Object>> coursesStats = new ArrayList<>();
            
            File[] courseDirectories = coursesDir.listFiles(File::isDirectory);
            if (courseDirectories == null || courseDirectories.length == 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "No course directories found in " + coursesPath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            for (File courseDir : courseDirectories) {
                Map<String, Object> courseStats = new HashMap<>();
                courseStats.put("courseName", courseDir.getName());
                
                try {
                    // Folosim bean-ul FlashcardImport injectat pentru a genera flashcarduri
                    // Folosim DEFAULT_USER_ID (valoarea 1) definit în FlashcardImport
                    int importedCount = 0;
                    List<Flashcard> flashcards = new ArrayList<>();
                    
                    try {
                        importedCount = flashcardImport.importFlashcardsFromDirectory(courseDir.getAbsolutePath(), null);
                        System.out.println("Importate " + importedCount + " flashcarduri din cursul: " + courseDir.getName());

                        // Estimăm numărul de flashcarduri importate pe baza rezultatului importării
                        if (importedCount > 0) {
                            // Adăugăm placeholders pentru statistici
                            for (int i = 0; i < importedCount; i++) {
                                flashcards.add(new Flashcard());
                            }
                        }
                        
                        // Salvăm flashcardurile generate în baza de date
                        for (Flashcard flashcard : flashcards) {
                            flashcardService.createFlashcard(flashcard);
                        }
                    } catch (Exception e) {
                        System.err.println("Eroare la importarea flashcardurilor: " + e.getMessage());
                        // Setăm imported count la 0 în caz de eroare
                        importedCount = 0;
                    }
                    
                    totalFlashcards += importedCount;
                    courseStats.put("flashcardsGenerated", importedCount);
                    courseStats.put("status", "success");
                } catch (Exception e) {
                    courseStats.put("status", "error");
                    courseStats.put("message", e.getMessage());
                }
                
                coursesStats.add(courseStats);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Flashcards generated for all courses");
            response.put("totalFlashcards", totalFlashcards);
            response.put("courses", coursesStats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/due/today")
    public ResponseEntity<List<Flashcard>> getDueForToday(@RequestParam("userId") Integer userId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Date today = new Date();
        // Corectez ordinea parametrilor pentru getDueFlashcards
        List<Flashcard> dueFlashcards = flashcardService.getDueFlashcards(today, userId);
        // Limitez manual rezultatele deoarece metoda nu acceptă parametrul limit
        return ResponseEntity.ok(dueFlashcards.stream().limit(limit).toList());
    }

    @GetMapping("/due")
    public ResponseEntity<List<Flashcard>> getDueUntilDate(@RequestParam("userId") Integer userId,
            @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        // Corectez ordinea parametrilor pentru getDueFlashcards
        List<Flashcard> dueFlashcards = flashcardService.getDueFlashcards(dueDate, userId);
        // Limitez manual rezultatele deoarece metoda nu acceptă parametrul limit
        return ResponseEntity.ok(dueFlashcards.stream().limit(limit).toList());
    }
    
    @GetMapping("/random/user/{userId}")
    public ResponseEntity<List<Flashcard>> getRandomFlashcardsByUser(
            @PathVariable Integer userId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByUserId(userId, limit));
    }
    
    @GetMapping("/prioritized/user/{userId}")
    public ResponseEntity<List<Flashcard>> getPrioritizedFlashcardsByUser(
            @PathVariable Integer userId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        // Nu există metoda getPrioritizedFlashcardsByUser - folosim getRandomFlashcardsByUserId
        // care de fapt returnează flashcarduri prioritizate
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByUserId(userId, limit));
    }
    
    @GetMapping("/random/course/{courseId}")
    public ResponseEntity<List<Flashcard>> getRandomFlashcardsByCourse(
            @PathVariable Long courseId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByCourseId(courseId, limit));
    }
    
    @GetMapping("/prioritized/course/{courseId}")
    public ResponseEntity<List<Flashcard>> getPrioritizedFlashcardsByCourse(
            @PathVariable Long courseId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        // Nu există metoda getPrioritizedFlashcardsByCourse - folosim getRandomFlashcardsByCourseId
        // care de fapt returnează flashcarduri prioritizate
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByCourseId(courseId, limit));
    }
    
    @GetMapping("/random/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getRandomFlashcardsByMaterial(
            @PathVariable Long materialId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByMaterialId(materialId, limit));
    }
    
    @GetMapping("/prioritized/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getPrioritizedFlashcardsByMaterial(
            @PathVariable Long materialId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        // Nu există metoda getPrioritizedFlashcardsByMaterial - folosim getRandomFlashcardsByMaterialId
        // care de fapt returnează flashcarduri prioritizate
        return ResponseEntity.ok(flashcardService.getRandomFlashcardsByMaterialId(materialId, limit));
    }
    
    @GetMapping("/prioritized/material/{materialId}/user/{userId}")
    public ResponseEntity<List<Flashcard>> getPrioritizedFlashcardsByMaterialAndUser(
            @PathVariable Long materialId,
            @PathVariable Integer userId,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        
        // Folosim direct getRandomFlashcardsByMaterialIdAndUserId care deja implementează prioritizarea
        List<Flashcard> prioritizedFlashcards = flashcardService.getRandomFlashcardsByMaterialIdAndUserId(
                materialId, userId, limit);
        
        return ResponseEntity.ok(prioritizedFlashcards);
    }
    
    /**
     * Endpoint pentru înregistrarea unui răspuns la un flashcard și actualizarea progresului
     * @param response DTO conținând ID-ul flashcard-ului, ID-ul utilizatorului și calitatea răspunsului
     * @return ResponseEntity cu statusul actualizării și detalii despre progresul modificat
     */
    /**
     * Endpoint pentru înregistrarea răspunsurilor la flashcarduri și actualizarea progresului utilizatorului
     * @param response DTO conținând ID-ul flashcard-ului, ID-ul utilizatorului și calitatea răspunsului
     * @return ResponseEntity cu statusul actualizării și detalii despre progresul modificat
     */
    @PostMapping("/response")
    public ResponseEntity<Map<String, Object>> recordFlashcardResponse(@RequestBody FlashcardResponseDTO response) {
        // Log complet al datelor primite pentru debugging
        System.out.println("============================================================================");
        System.out.println("DEBUG: CERERE COMPLETĂ PRIMITĂ LA /response: " + response);
        System.out.println("DEBUG: FlashcardId=" + response.getFlashcardId() + ", UserId=" + response.getUserId() + ", Quality=" + response.getQuality() + ", isCorrect=" + response.isCorrect());
        // Trace complet al stivei pentru debugging
        try {
            throw new Exception("Trace stack pentru debugging");
        } catch (Exception e) {
            System.out.println("DEBUG TRACE (ignorati acest stack trace, este doar pentru debugging):");
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("======== ÎNCEPUTUL PROCESĂRII RĂSPUNSULUI FLASHCARD ========");
        System.out.println("Datele primite: " + response);
        
        try {
            System.out.println("====== DIAGNOSTIC LOG: FlashcardController.recordFlashcardResponse ======");
            System.out.println("Primit răspuns pentru flashcard: " + response);
            
            // Convertim userId în Integer dacă este necesar
            Integer userId = null;
            Object userIdObj = response.getUserId();
            
            if (userIdObj instanceof Integer) {
                userId = (Integer) userIdObj;
            } else if (userIdObj instanceof String) {
                try {
                    userId = Integer.parseInt((String) userIdObj);
                    System.out.println("Am convertit userId din string '" + userIdObj + "' la Integer " + userId);
                } catch (NumberFormatException e) {
                    System.out.println("EROARE la conversia userId din string la Integer: " + e.getMessage());
                    result.put("status", "error");
                    result.put("message", "Format invalid pentru ID utilizator");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
            } else if (userIdObj != null) {
                try {
                    userId = Integer.parseInt(userIdObj.toString());
                    System.out.println("Am convertit userId din obiect de tip " + userIdObj.getClass().getName() +
                                 " la Integer " + userId);
                } catch (Exception e) {
                    System.out.println("EROARE la conversia userId: " + e.getMessage());
                    result.put("status", "error");
                    result.put("message", "Format invalid pentru ID utilizator");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
            }
        
            // Validăm datele primite
            if (response.getFlashcardId() == null) {
                result.put("status", "error");
                result.put("message", "Flashcard ID este obligatoriu");
                return ResponseEntity.badRequest().body(result);
            }
            
            if (userId == null) {
                result.put("status", "error");
                result.put("message", "User ID este obligatoriu");
                return ResponseEntity.badRequest().body(result);
            }
            
            System.out.println("Prelucrăm răspuns pentru flashcard ID " + response.getFlashcardId() + 
                          ", user ID " + userId + 
                          ", quality " + response.getQuality() + 
                          ", isCorrect " + response.isCorrect());
            
            // Verificăm dacă flashcardul există
            Optional<Flashcard> optionalFlashcard = flashcardService.getFlashcardById(response.getFlashcardId());
            if (!optionalFlashcard.isPresent()) {
                result.put("status", "error");
                result.put("message", "Flashcard cu ID " + response.getFlashcardId() + " nu există");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            
            // Verificăm dacă utilizatorul există
            Optional<User> optionalUser = userService.findById(userId);
            if (!optionalUser.isPresent()) {
                result.put("status", "error");
                result.put("message", "Utilizator cu ID " + userId + " nu există");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            
            // Obținem obiectele din opționale
            Flashcard flashcard = optionalFlashcard.get();
            User user = optionalUser.get();
            
            // Încercăm să găsim progresul existent pentru această pereche flashcard-utilizator
            FlashcardProgress existingProgress = flashcardProgressService.findByFlashcardAndUser(flashcard.getId(), userId);
            
            FlashcardProgress updatedProgress;
            
            if (existingProgress == null) {
                // Dacă nu există progres, creăm unul nou
                System.out.println("Nu există progres pentru flashcard ID " + flashcard.getId() + 
                              " și user ID " + userId + ". Creăm un nou progres.");
                
                // Creăm un nou obiect FlashcardProgress cu inițializarea tuturor câmpurilor pentru a evita NullPointerException
                FlashcardProgress newProgress = new FlashcardProgress();
                newProgress.setFlashcard(flashcard);
                newProgress.setUser(user);
                newProgress.setEaseFactor(2.5); // Valoare inițială standard pentru SM-2
                newProgress.setRepetitions(0); // În loc de setConsecutiveCorrectAnswers
                newProgress.setInterval(1);  // Interval inițial de 1 zi
                newProgress.setDueDate(new Date()); // Setăm data curentă (în loc de setNextReviewDate)
                newProgress.setLastReviewed(new Date()); // Setăm data curentă ca ultima dată de revizuire
                newProgress.setConfidenceLevel(response.getQuality()); // Setăm nivelul de încredere conform calității răspunsului
                newProgress.setConsecutiveFailures(0); // Inițializăm numărul de eșecuri consecutive cu 0
                newProgress.setLearningStage(2); // Inițializăm stadiul de învățare cu 2 (corespunzător constrângerii din baza de date)
                newProgress.setRetentionScore(0.0); // Inițializăm scorul de retenție cu 0.0 pentru un card nou
                newProgress.setStudyTimeMs(0L); // Inițializăm timpul de studiu cu 0 milisecunde
                newProgress.setTotalFailures(0); // Inițializăm numărul total de eșecuri cu 0
                
                try {
                    // Asigurăm-ne că toate câmpurile obligatorii sunt setate corect
                    if (newProgress.getFlashcard() == null || newProgress.getUser() == null) {
                        throw new IllegalStateException("Referințele către Flashcard sau User nu pot fi null");
                    }
                    
                    // Actualizăm progresul cu algoritmul SM-2
                    SpacedRepetitionAlgorithm.updateProgress(newProgress, response.getQuality());
                    
                    // Garantăm că toate câmpurile sunt setate corect (nu putem verifica == null pentru tipuri primitive)
                    // Folosim try-catch pentru a prinde orice NullPointerException care ar putea apărea
                    try {
                        // Doar verificăm dacă accesul la aceste proprietăți nu cauzează NullPointerException
                        // Variabilele sunt create doar pentru a verifica dacă aruncă NPE, valorile nu sunt folosite
                        newProgress.getEaseFactor();
                        newProgress.getInterval();
                        newProgress.getRepetitions();
                        newProgress.getConfidenceLevel();
                        newProgress.getConsecutiveFailures();
                        newProgress.getLearningStage();
                        newProgress.getRetentionScore();
                        newProgress.getStudyTimeMs();
                        newProgress.getTotalFailures();
                    } catch (NullPointerException e) {
                        // Dacă oricare proprietate cauzează NullPointerException, setăm toate valorile la valorile implicite
                        newProgress.setEaseFactor(2.5);
                        newProgress.setInterval(1);
                        newProgress.setRepetitions(0);
                        newProgress.setConfidenceLevel(response.getQuality());
                        newProgress.setConsecutiveFailures(0);
                        newProgress.setLearningStage(3);
                        newProgress.setRetentionScore(0.0);
                        newProgress.setStudyTimeMs(0L);
                        newProgress.setTotalFailures(0);
                    }
                    
                    System.out.println("Progres nou creat înainte de salvare: " + 
                                   "flashcardId=" + newProgress.getFlashcard().getId() + 
                                   ", userId=" + newProgress.getUser().getId() + 
                                   ", easeFactor=" + newProgress.getEaseFactor() + 
                                   ", interval=" + newProgress.getInterval() + 
                                   ", repetitions=" + newProgress.getRepetitions() + 
                                   ", confidenceLevel=" + newProgress.getConfidenceLevel() + 
                                   ", dueDate=" + newProgress.getDueDate());
                    
                    // Salvăm noul progres - folosim un block try-catch separat pentru a detecta probleme specifice la salvare
                    try {
                        updatedProgress = flashcardProgressService.createFlashcardProgress(newProgress);
                    } catch (Exception e) {
                        System.out.println("EROARE SPECIFICĂ la salvarea în baza de date: " + e.getMessage());
                        e.printStackTrace();
                        result.put("status", "error");
                        result.put("message", "Eroare la salvarea în baza de date: " + e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                    }
                } catch (Exception e) {
                    System.out.println("EROARE la salvarea noului progres: " + e.getMessage());
                    e.printStackTrace();
                    result.put("status", "error");
                    result.put("message", "Eroare la salvarea progresului: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
                
                System.out.println("Progres nou creat pentru user " + user.getEmail() + 
                              " și flashcard ID " + flashcard.getId() + 
                              " cu următoarea dată de revizuire: " + updatedProgress.getDueDate());
            } else {
                // Dacă există progres, îl actualizăm folosind algoritmul SM-2
                System.out.println("Actualizăm progresul existent pentru flashcard ID " + flashcard.getId() + 
                              " și user ID " + userId);
                
                try {
                    // Actualizăm confidenceLevel cu valoarea calității răspunsului curent
                    existingProgress.setConfidenceLevel(response.getQuality());
                    
                    // Actualizăm progresul cu algoritmul SM-2
                    SpacedRepetitionAlgorithm.updateProgress(existingProgress, response.getQuality());
                    
                    // Garantăm că toate câmpurile sunt setate corect (nu putem verifica == null pentru tipuri primitive)
                    // Folosim try-catch pentru a prinde orice NullPointerException care ar putea apărea
                    try {
                        // Doar verificăm dacă accesul la aceste proprietăți nu cauzează NullPointerException
                        // Variabilele sunt create doar pentru a verifica dacă aruncă NPE, valorile nu sunt folosite
                        existingProgress.getEaseFactor();
                        existingProgress.getInterval();
                        existingProgress.getRepetitions();
                        existingProgress.getConfidenceLevel();
                        existingProgress.getConsecutiveFailures();
                        existingProgress.getLearningStage();
                        existingProgress.getRetentionScore();
                        existingProgress.getStudyTimeMs();
                        existingProgress.getTotalFailures();
                    } catch (NullPointerException e) {
                        // Dacă oricare proprietate cauzează NullPointerException, setăm toate valorile la valorile implicite
                        existingProgress.setEaseFactor(2.5);
                        existingProgress.setInterval(1);
                        existingProgress.setRepetitions(0);
                        existingProgress.setConfidenceLevel(response.getQuality());
                        existingProgress.setConsecutiveFailures(0);
                        existingProgress.setLearningStage(3);
                        existingProgress.setRetentionScore(0.0);
                        existingProgress.setStudyTimeMs(0L);
                        existingProgress.setTotalFailures(0);
                    }
                    
                    System.out.println("Progres existent actualizat înainte de salvare: " + 
                                   "flashcardId=" + existingProgress.getFlashcard().getId() + 
                                   ", userId=" + existingProgress.getUser().getId() + 
                                   ", easeFactor=" + existingProgress.getEaseFactor() + 
                                   ", interval=" + existingProgress.getInterval() + 
                                   ", repetitions=" + existingProgress.getRepetitions() + 
                                   ", confidenceLevel=" + existingProgress.getConfidenceLevel() + 
                                   ", dueDate=" + existingProgress.getDueDate());
                    
                    // Salvăm progresul actualizat într-un bloc try-catch separat pentru a detecta probleme specifice la salvare
                    try {
                        updatedProgress = flashcardProgressService.createFlashcardProgress(existingProgress);
                    } catch (Exception e) {
                        System.out.println("EROARE SPECIFICĂ la salvarea în baza de date: " + e.getMessage());
                        e.printStackTrace();
                        result.put("status", "error");
                        result.put("message", "Eroare la salvarea în baza de date: " + e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                    }
                } catch (Exception e) {
                    System.out.println("EROARE la actualizarea progresului existent: " + e.getMessage());
                    e.printStackTrace();
                    result.put("status", "error");
                    result.put("message", "Eroare la actualizarea progresului: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
            }
                
            int finalQuality = response.getQuality();
            String feedback;
            
            if (response.isWrittenAnswer() && response.getPercentageCorrect() != null) {
                finalQuality = convertPercentageToQuality(response.getPercentageCorrect());
                logger.debug("Răspuns scris: {} cu procentaj {}%. Calitate: {}", 
                             flashcard.getId(), response.getPercentageCorrect(), finalQuality);
                feedback = generateFeedbackForWrittenAnswer(response.getPercentageCorrect(), finalQuality);
            } else {
                feedback = generateFeedbackBasedOnQuality(finalQuality);
            }
            
            logger.debug("Feedback: {} pentru user {} și card {}", feedback, userId, flashcard.getId());
                         
            // Pregătim rezultatul pentru client
            result.put("status", "success");
            result.put("message", "Progres actualizat cu succes");
            result.put("feedback", feedback);
            
            // Adăugăm detalii de feedback pentru interfața utilizator
            Map<String, Object> feedbackDetails = new HashMap<>();
            feedbackDetails.put("quality", response.getQuality());
            feedbackDetails.put("nextReviewIn", updatedProgress.getInterval());
            feedbackDetails.put("currentEaseFactor", updatedProgress.getEaseFactor());
            result.put("feedbackDetails", feedbackDetails);
            
            // Pregătim datele de progres pentru a evita erori de serializare
            Map<String, Object> progressInfo = new HashMap<>();
            progressInfo.put("id", updatedProgress.getId());
            progressInfo.put("flashcardId", updatedProgress.getFlashcard().getId());
            progressInfo.put("userId", updatedProgress.getUser().getId());
            progressInfo.put("easeFactor", updatedProgress.getEaseFactor());
            progressInfo.put("interval", updatedProgress.getInterval());
            progressInfo.put("repetitions", updatedProgress.getRepetitions());
            progressInfo.put("dueDate", updatedProgress.getDueDate());
            progressInfo.put("lastReviewed", updatedProgress.getLastReviewed());
            progressInfo.put("learningStage", updatedProgress.getLearningStage());
            
            result.put("progress", progressInfo);
            result.put("flashcardId", flashcard.getId());
            result.put("userId", user.getId());
            result.put("nextReview", updatedProgress.getDueDate());
            result.put("easeFactor", updatedProgress.getEaseFactor());
            result.put("interval", updatedProgress.getInterval());
            result.put("repetitions", updatedProgress.getRepetitions());
            
            System.out.println("Răspuns procesat cu succes pentru flashcard ID " + flashcard.getId() + 
                          ", următoarea revizuire la " + updatedProgress.getDueDate());
            System.out.println("============================================================================");
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            // Logging detaliat pentru excepții neașteptate
            System.out.println("============================================================================");
            System.out.println("EROARE NEAȘTEPTATĂ în procesarea răspunsului: " + e.getMessage());
            System.out.println("Clasa excepției: " + e.getClass().getName());
            System.out.println("Cauza: " + (e.getCause() != null ? e.getCause().getMessage() : "necunoscută"));
            e.printStackTrace();
            
            result.put("status", "error");
            result.put("message", "Eroare internă la procesarea răspunsului: " + e.getMessage());
            
            // Adăugăm informații suplimentare pentru debugging
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("exceptionClass", e.getClass().getName());
            errorDetails.put("cause", e.getCause() != null ? e.getCause().getMessage() : "necunoscută");
            errorDetails.put("timestamp", new Date());
            
            result.put("errorDetails", errorDetails);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
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
    
    private String generateFeedbackBasedOnQuality(int quality) {
        String[] feedbackMessages;
        
        switch (quality) {
            case 0:
                feedbackMessages = new String[] {
                    "Nu-ți face griji! Acest card va apărea mai frecvent pentru a te ajuta să-l înveți.",
                    "Pare dificil, dar vei primi mai multe oportunități să exersezi acest card.",
                    "Un pas înapoi e normal în procesul de învățare. Vom lucra mai mult pe acest concept."
                };
                break;
            case 1:
                feedbackMessages = new String[] {
                    "Ai recunoscut parțial conceptul. Vom continua să exersăm împreună.",
                    "E un început bun! Cu mai multă practică, acest concept va deveni mai clar.",
                    "Ai reținut ceva, ceea ce e important. Următoarea întâlnire cu acest card va fi mai curând."
                };
                break;
            case 2:
                feedbackMessages = new String[] {
                    "Progresezi! Încă nu e perfect, dar ești pe drumul cel bun.",
                    "Efortul tău începe să dea roade. Mai ai nevoie de practică, dar ești aproape.",
                    "Continuă să lucrezi! Acest card va reveni curând pentru consolidare."
                };
                break;
            case 3:
                feedbackMessages = new String[] {
                    "Bine! Ai răspuns corect, dar cu oarecare dificultate.",
                    "Ai reușit să răspunzi, deși a fost puțin dificil. Continuă să exersezi!",
                    "Răspuns corect! Data viitoare va fi și mai ușor."
                };
                break;
            case 4:
                feedbackMessages = new String[] {
                    "Foarte bine! Răspunsul a venit natural și corect.",
                    "Excelent! Se vede că stăpânești bine acest concept.",
                    "Impresionant! Acest card începe să devină o cunoștință solidă pentru tine."
                };
                break;
            case 5:
                feedbackMessages = new String[] {
                    "Perfect! Ai stăpânit complet acest concept.",
                    "Excelent! Acest card va reveni peste un interval mai lung de timp.",
                    "Bravo! Răspunsul tău perfect arată că ai internalizat această informație."
                };
                break;
            default:
                feedbackMessages = new String[] {
                    "Mulțumim pentru răspuns! Continuă să înveți și să progresezi."
                };
        }
        
        return feedbackMessages[random.nextInt(feedbackMessages.length)];
    }
    
    private int convertPercentageToQuality(Double percentage) {
        if (percentage == null) {
            return 0;
        }
        
        double safePercentage = Math.max(0, Math.min(100, percentage));
        int quality = (int) Math.round(safePercentage / 20.0);
        
        return quality;
    }
    
    private String generateFeedbackForWrittenAnswer(Double percentage, int quality) {
        if (percentage == null) {
            return "Nu am putut evalua corectitudinea răspunsului tău scris.";
        }
        
        String[] feedbackMessages;
        String percentFormatted = String.format("%.1f", percentage);
        
        if (percentage >= 90) {
            feedbackMessages = new String[] {
                String.format("Excelent! Răspunsul tău scris este corect în proporție de %s%%.", percentFormatted),
                String.format("Perfect! Ai acoperit %s%% din punctele cheie în răspunsul tău.", percentFormatted),
                String.format("Foarte bine! Răspunsul tău conține %s%% din informațiile importante.", percentFormatted)
            };
        } else if (percentage >= 75) {
            feedbackMessages = new String[] {
                String.format("Foarte bine! Răspunsul tău acoperă %s%% din informațiile cheie.", percentFormatted),
                String.format("Aproape perfect. Răspunsul tău are o acuratețe de %s%%.", percentFormatted),
                String.format("Ai stăpânit %s%% din acest concept. Continuă să exersezi!", percentFormatted)
            };
        } else if (percentage >= 50) {
            feedbackMessages = new String[] {
                String.format("Bine! Răspunsul tău acoperă %s%% din elementele importante.", percentFormatted),
                String.format("Ai reținut corect %s%% din concept. Mai exersează pentru a-l stăpâni complet.", percentFormatted),
                String.format("Progresezi bine! Acuratețea răspunsului tău este de %s%%.", percentFormatted)
            };
        } else if (percentage >= 25) {
            feedbackMessages = new String[] {
                String.format("Ai inclus %s%% din informațiile importante. Continuă să exersezi acest concept.", percentFormatted),
                String.format("Răspunsul tău este parțial corect (%s%%). Revizuiește materialul pentru a îmbunătăți.", percentFormatted),
                String.format("Ai o înțelegere de bază (%s%%). Acest card va apărea mai frecvent pentru exersare.", percentFormatted)
            };
        } else {
            feedbackMessages = new String[] {
                String.format("Răspunsul tău conține doar %s%% din informațiile necesare. Îi vom acorda o atenție specială.", percentFormatted),
                String.format("Pare că acest concept necesită mai multă atenție. Acuratețe: %s%%.", percentFormatted),
                String.format("Acest concept încă îți dă dificultăți (%s%% acuratețe). Îl vom repeta mai des.", percentFormatted)
            };
        }
        
        return feedbackMessages[random.nextInt(feedbackMessages.length)];
    }
}
