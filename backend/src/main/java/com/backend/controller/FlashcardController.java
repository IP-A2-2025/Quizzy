
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/Flashcard")

public class FlashcardController {

    private final FlashcardService flashcardService;


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

    }

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard) {

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Flashcard>> getByUserId(@PathVariable Integer userId) {

    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByMaterialId(@PathVariable Long materialId) {

}
