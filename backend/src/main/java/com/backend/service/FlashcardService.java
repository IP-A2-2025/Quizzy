package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.model.FlashcardProgress;
import com.backend.repository.FlashcardRepository;
import com.backend.utils.FlashcardPrioritizer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardProgressService flashcardProgressService;

    public FlashcardService(FlashcardRepository flashcardRepository, FlashcardProgressService flashcardProgressService) {
        this.flashcardRepository = flashcardRepository;
        this.flashcardProgressService = flashcardProgressService;
    }

    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    public Optional<Flashcard> getFlashcardById(Long id) {
        return flashcardRepository.findById(id);
    }

    public Flashcard createFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public Flashcard updateFlashcard(Long id, Flashcard updatedFlashcard) {
        return flashcardRepository.findById(id).map(flashcard -> {
            flashcard.setQuestion(updatedFlashcard.getQuestion());
            flashcard.setLevel(updatedFlashcard.getLevel());
            flashcard.setLastStudiedAt(updatedFlashcard.getLastStudiedAt());
            flashcard.setQuestionType(updatedFlashcard.getQuestionType());
            flashcard.setPageIndex(updatedFlashcard.getPageIndex());
            flashcard.setUser(updatedFlashcard.getUser());
            flashcard.setMaterial(updatedFlashcard.getMaterial());
            flashcard.setAnswers(updatedFlashcard.getAnswers());
            return flashcardRepository.save(flashcard);
        }).orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
    }

    public void deleteFlashcard(Long id) {
        flashcardRepository.deleteById(id);
    }

    public List<Flashcard> getByUserId(Integer userId) {
        return flashcardRepository.findByUserId(userId);
    }

    public List<Flashcard> getByMaterialId(Long materialId) {
        return flashcardRepository.findByMaterialId(materialId);
    }

    public List<Flashcard> getDueFlashcards(Date date, Integer userId) {
        return flashcardRepository.findDueFlashcards(date, userId);
    }

    public List<Flashcard> getByLevelAndUserId(int level, Integer userId) {
        return flashcardRepository.findByLevelAndUserId(level, userId);
    }

    public List<Flashcard> getByCourseIdAndUserId(Long courseId, Integer userId) {
        return flashcardRepository.findByCourseIdAndUserId(courseId, userId);
    }

    public List<Flashcard> getByQuestionTypeAndUserId(String type, Integer userId) {
        return flashcardRepository.findByQuestionTypeAndUserId(type, userId);
    }
    
    public List<Flashcard> getByPageIndex(Integer pageIndex) {
        return flashcardRepository.findByPageIndex(pageIndex);
    }
    
    public List<Flashcard> getByPageIndexAndUserId(Integer pageIndex, Integer userId) {
        return flashcardRepository.findByPageIndexAndUserId(pageIndex, userId);
    }
    
    public List<Flashcard> getByPageIndexAndMaterialId(Integer pageIndex, Long materialId) {
        return flashcardRepository.findByPageIndexAndMaterialId(pageIndex, materialId);
    }

    public List<Flashcard> getFlashcardsByCourseId(Long courseId) {
        return flashcardRepository.findAllByCourseId(courseId);
    }
    
    /**
     * Retrieves a limited number of prioritized flashcards
     * @param limit The maximum number of flashcards to return
     * @return A list of prioritized flashcards
     */
    public List<Flashcard> getRandomFlashcards(int limit) {
        List<Flashcard> allFlashcards = flashcardRepository.findAllFlashcards();
        // Nu avem un utilizator specific, deci folosim doar algoritmul de bază
        List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(allFlashcards, limit);
        
        System.out.println("Retrieved " + prioritizedFlashcards.size() + " prioritized flashcards");
        System.out.println("Selected flashcard IDs: " + 
            prioritizedFlashcards.stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("none"));
        return prioritizedFlashcards;
    }
    
    /**
     * Retrieves a limited number of prioritized flashcards for a specific user
     * @param userId The user ID
     * @param limit The maximum number of flashcards to return
     * @return A list of prioritized flashcards for the user
     */
    public List<Flashcard> getRandomFlashcardsByUserId(Integer userId, int limit) {
        List<Flashcard> allUserFlashcards = flashcardRepository.findAllFlashcardsByUserId(userId);
        
        // Obținem datele de progres pentru acest utilizator
        List<FlashcardProgress> userProgress = flashcardProgressService.getByUserId(userId);
        
        // Folosim datele de progres pentru prioritizare
        List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(
            allUserFlashcards, userProgress, userId, limit);
        
        System.out.println("Retrieved " + prioritizedFlashcards.size() + " prioritized flashcards for user " + userId);
        System.out.println("Selected flashcard IDs for user " + userId + ": " + 
            prioritizedFlashcards.stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("none"));
        return prioritizedFlashcards;
    }
    
    /**
     * Retrieves a limited number of prioritized flashcards for a specific course
     * @param courseId The course ID
     * @param limit The maximum number of flashcards to return
     * @return A list of prioritized flashcards from the course
     */
    public List<Flashcard> getRandomFlashcardsByCourseId(Long courseId, int limit) {
        List<Flashcard> allCourseFlashcards = flashcardRepository.findAllFlashcardsByCourseId(courseId);
        
        // Pentru cursuri, nu avem un utilizator specific, deci folosim prioritizarea de bază
        List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(allCourseFlashcards, limit);
        
        System.out.println("Retrieved " + prioritizedFlashcards.size() + " prioritized flashcards for course " + courseId);
        System.out.println("Selected flashcard IDs for course " + courseId + ": " + 
            prioritizedFlashcards.stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("none"));
        return prioritizedFlashcards;
    }
    
    /**
     * Retrieves a limited number of prioritized flashcards for a specific material
     * @param materialId The material ID
     * @param limit The maximum number of flashcards to return
     * @return A list of prioritized flashcards from the material
     */
    public List<Flashcard> getRandomFlashcardsByMaterialId(Long materialId, int limit) {
        List<Flashcard> allMaterialFlashcards = flashcardRepository.findAllFlashcardsByMaterialId(materialId);
        
        // Pentru materiale, nu avem un utilizator specific, deci folosim prioritizarea de bază
        List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(allMaterialFlashcards, limit);
        
        System.out.println("Retrieved " + prioritizedFlashcards.size() + " prioritized flashcards for material " + materialId);
        System.out.println("Selected flashcard IDs for material " + materialId + ": " + 
            prioritizedFlashcards.stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("none"));
        return prioritizedFlashcards;
    }
    
    /**
     * Retrieves a limited number of prioritized flashcards for a specific material and user
     * Integrates user's progress data for more personalized prioritization
     * 
     * @param materialId The material ID
     * @param userId The user ID
     * @param limit The maximum number of flashcards to return
     * @return A list of prioritized flashcards from the material for the user
     */
    public List<Flashcard> getRandomFlashcardsByMaterialIdAndUserId(Long materialId, Integer userId, int limit) {
        List<Flashcard> allMaterialUserFlashcards = flashcardRepository.findAllFlashcardsByMaterialIdAndUserId(materialId, userId);
        
        System.out.println("====== DIAGNOSTIC LOG: FlashcardService.getRandomFlashcardsByMaterialIdAndUserId ======");
        System.out.println("MaterialId: " + materialId + ", UserId: " + userId + ", Limit: " + limit);
        System.out.println("Total flashcards found for material and user: " + 
                         (allMaterialUserFlashcards != null ? allMaterialUserFlashcards.size() : 0));
        
        // Verificăm dacă utilizatorul are flashcarduri pentru acest material
        if (allMaterialUserFlashcards == null || allMaterialUserFlashcards.isEmpty()) {
            System.out.println("ATENȚIE: Nu s-au găsit flashcarduri pentru materialul " + materialId + 
                           " și utilizatorul " + userId);
            System.out.println("Verificăm dacă există flashcarduri pentru acest material în general...");
            List<Flashcard> allMaterialFlashcards = flashcardRepository.findAllFlashcardsByMaterialId(materialId);
            System.out.println("Total flashcarduri pentru materialul " + materialId + ": " + 
                             (allMaterialFlashcards != null ? allMaterialFlashcards.size() : 0));
            
            if (allMaterialFlashcards != null && !allMaterialFlashcards.isEmpty()) {
                System.out.println("NOTĂ: Există flashcarduri pentru acest material, dar nu sunt asociate utilizatorului " + userId);
                System.out.println("Primul flashcard din material: ID=" + allMaterialFlashcards.get(0).getId() + 
                               ", UserId=" + (allMaterialFlashcards.get(0).getUser() != null ? 
                                            allMaterialFlashcards.get(0).getUser().getId() : "null"));
                
                // SOLUȚIE: Folosim toate flashcardurile materialului, indiferent de utilizator
                System.out.println("SOLUȚIE IMPLEMENTATĂ: Returnăm toate flashcardurile materialului, indiferent de utilizator");
                
                // Obținem datele de progres pentru acest utilizator și material (probabil goală)
                List<FlashcardProgress> userMaterialProgress = flashcardProgressService.getByUserIdAndMaterialId(userId, materialId);
                
                // Folosim datele de progres pentru prioritizare (sau algoritmul de bază dacă nu există progres)
                List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(
                    allMaterialFlashcards, userMaterialProgress, userId, limit);
                
                System.out.println("Flashcarduri prioritizate returnate din toate flashcardurile materialului: " + 
                                 (prioritizedFlashcards != null ? prioritizedFlashcards.size() : 0));
                if (prioritizedFlashcards != null && !prioritizedFlashcards.isEmpty()) {
                    System.out.println("ID-urile flashcardurilor selectate: " + 
                        prioritizedFlashcards.stream()
                            .map(f -> f.getId().toString())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("none"));
                }
                System.out.println("============================================================================");
                
                return prioritizedFlashcards;
            }
            
            return new ArrayList<>(); // Returnăm o listă goală dacă nu există flashcarduri pentru material
        }
        
        // Dacă utilizatorul are flashcarduri pentru acest material, continuăm normal
        // Obținem datele de progres pentru acest utilizator și material
        List<FlashcardProgress> userMaterialProgress = flashcardProgressService.getByUserIdAndMaterialId(userId, materialId);
        System.out.println("Total înregistrări de progres pentru material și utilizator: " + 
                         (userMaterialProgress != null ? userMaterialProgress.size() : 0));
        
        // Folosim datele de progres pentru prioritizare
        List<Flashcard> prioritizedFlashcards = FlashcardPrioritizer.getPrioritizedFlashcards(
            allMaterialUserFlashcards, userMaterialProgress, userId, limit);
        
        System.out.println("Flashcarduri prioritizate returnate: " + 
                         (prioritizedFlashcards != null ? prioritizedFlashcards.size() : 0));
        System.out.println("ID-urile flashcardurilor selectate: " + 
            prioritizedFlashcards.stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("none"));
        System.out.println("============================================================================");
        
        return prioritizedFlashcards;
    }
}