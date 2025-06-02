package com.backend.repository;

import com.backend.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    @Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId")
    List<Flashcard> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT f FROM Flashcard f WHERE f.material.id = :materialId")
    List<Flashcard> findByMaterialId(@Param("materialId") Long materialId);

    @Query("SELECT f FROM Flashcard f WHERE f.lastStudiedAt <= :date AND f.user.id = :userId")
    List<Flashcard> findDueFlashcards(@Param("date") Date date, @Param("userId") Integer userId);

    @Query("SELECT f FROM Flashcard f WHERE f.level = :level AND f.user.id = :userId")
    List<Flashcard> findByLevelAndUserId(@Param("level") Integer level, @Param("userId") Integer userId);

    @Query("SELECT f FROM Flashcard f WHERE f.material.course.id = :courseId AND f.user.id = :userId")
    List<Flashcard> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Integer userId);

    @Query("SELECT f FROM Flashcard f WHERE f.questionType = :type AND f.user.id = :userId")
    List<Flashcard> findByQuestionTypeAndUserId(@Param("type") String type, @Param("userId") Integer userId);

    @Query("SELECT f FROM Flashcard f WHERE f.question = :questionText")
    Flashcard findByQuestion(@Param("questionText") String questionText);
    
    @Query("SELECT f FROM Flashcard f WHERE f.pageIndex = :pageIndex")
    List<Flashcard> findByPageIndex(@Param("pageIndex") Integer pageIndex);
    
    @Query("SELECT f FROM Flashcard f WHERE f.pageIndex = :pageIndex AND f.user.id = :userId")
    List<Flashcard> findByPageIndexAndUserId(
            @Param("pageIndex") Integer pageIndex, 
            @Param("userId") Integer userId);
    
    @Query("SELECT f FROM Flashcard f WHERE f.pageIndex = :pageIndex AND f.material.id = :materialId")
    List<Flashcard> findByPageIndexAndMaterialId(
            @Param("pageIndex") Integer pageIndex, 
            @Param("materialId") Long materialId);

    @Query("SELECT COUNT(f) FROM Flashcard f WHERE f.material.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT f FROM Flashcard f WHERE f.material.course.id = :courseId")
    List<Flashcard> findAllByCourseId(@Param("courseId") Long courseId);
    
    /**
     * Retrieves all flashcards, to be randomized in service layer
     * @return All flashcards
     */
    @Query("SELECT f FROM Flashcard f")
    List<Flashcard> findAllFlashcards();
    
    /**
     * Retrieves all flashcards for a specific user, to be randomized in service layer
     * @param userId The user ID
     * @return All flashcards for the user
     */
    @Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId")
    List<Flashcard> findAllFlashcardsByUserId(@Param("userId") Integer userId);
    
    /**
     * Retrieves all flashcards for a specific course, to be randomized in service layer
     * @param courseId The course ID
     * @return All flashcards from the course
     */
    @Query("SELECT f FROM Flashcard f WHERE f.material.course.id = :courseId")
    List<Flashcard> findAllFlashcardsByCourseId(@Param("courseId") Long courseId);
    
    /**
     * Retrieves all flashcards for a specific material, to be randomized in service layer
     * @param materialId The material ID
     * @return All flashcards from the material
     */
    @Query("SELECT f FROM Flashcard f WHERE f.material.id = :materialId")
    List<Flashcard> findAllFlashcardsByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * Retrieves all flashcards for a specific material and user, to be prioritized in service layer
     * @param materialId The material ID
     * @param userId The user ID
     * @return All flashcards from the material for the specific user
     */
    @Query("SELECT f FROM Flashcard f WHERE f.material.id = :materialId AND f.user.id = :userId")
    List<Flashcard> findAllFlashcardsByMaterialIdAndUserId(
            @Param("materialId") Long materialId, 
            @Param("userId") Integer userId);
}