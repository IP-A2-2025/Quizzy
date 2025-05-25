package com.backend.service;import com.backend.model.Grade;import com.backend.model.GradeId;import com.backend.model.TestEntity;import com.backend.model.User;import com.backend.repository.GradeRepository;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import java.util.Arrays;import java.util.Date;import java.util.List;import java.util.Optional;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.any;import static org.mockito.Mockito.*;class GradeServiceTest {    @Mock    private GradeRepository gradeRepository;    @InjectMocks    private GradeService gradeService;    private Grade testGrade;    private User testUser;    private TestEntity testEntity;    private GradeId gradeId;    private Date submissionDate;    @BeforeEach    void setUp() {        MockitoAnnotations.openMocks(this);        submissionDate = new Date();                testUser = new User();        testUser.setId(1);        testUser.setFirstName("John");        testUser.setLastName("Doe");        testEntity = new TestEntity();        testEntity.setId(1L);        testEntity.setTitle("Math Test");        gradeId = new GradeId();        gradeId.setTestID(testEntity.getId());        gradeId.setUserID(testUser.getId());        testGrade = new Grade();        testGrade.setId(gradeId);        testGrade.setGrade(90.5F);        testGrade.setSubmissionDate(submissionDate);        testGrade.setUser(testUser);        testGrade.setTest(testEntity);    }    @Test    void addGrade() {        when(gradeRepository.save(testGrade)).thenReturn(testGrade);        Grade result = gradeService.addGrade(testGrade);        assertEquals(testGrade, result);        verify(gradeRepository).save(testGrade);    }    @Test    void getGrade() {        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.of(testGrade));        Optional<Grade> result = gradeService.getGrade(testEntity.getId(), testUser.getId());        assertTrue(result.isPresent());        assertEquals(testGrade, result.get());        verify(gradeRepository).findById(any(GradeId.class));    }    @Test    void getGradeNotFound() {        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.empty());        Optional<Grade> result = gradeService.getGrade(999L, 999);        assertTrue(result.isEmpty());    }    @Test    void updateGrade() {        Grade updatedGrade = new Grade();        updatedGrade.setGrade(95.0F);        updatedGrade.setSubmissionDate(new Date());        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.of(testGrade));        when(gradeRepository.save(any(Grade.class))).thenReturn(testGrade);        Optional<Grade> result = gradeService.updateGrade(testEntity.getId(), testUser.getId(), updatedGrade);        assertTrue(result.isPresent());        verify(gradeRepository).findById(any(GradeId.class));        verify(gradeRepository).save(any(Grade.class));    }    @Test    void updateGradeWhenGradeNotFound() {        Grade updatedGrade = new Grade();        updatedGrade.setGrade(95.0F);        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.empty());        Optional<Grade> result = gradeService.updateGrade(999L, 999, updatedGrade);        assertTrue(result.isEmpty());        verify(gradeRepository).findById(any(GradeId.class));        verify(gradeRepository, never()).save(any(Grade.class));    }    @Test    void deleteGrade() {        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.of(testGrade));        doNothing().when(gradeRepository).delete(testGrade);        gradeService.deleteGrade(testEntity.getId(), testUser.getId());        verify(gradeRepository).findById(any(GradeId.class));        verify(gradeRepository).delete(testGrade);    }    @Test    void deleteGradeWhenNotFound() {        when(gradeRepository.findById(any(GradeId.class))).thenReturn(Optional.empty());        gradeService.deleteGrade(999L, 999);        verify(gradeRepository).findById(any(GradeId.class));        verify(gradeRepository, never()).delete(any(Grade.class));    }
    @Test
    void getGradesByUserId() {
        List<Grade> grades = Arrays.asList(testGrade);
        when(gradeRepository.findByUserId(testUser.getId())).thenReturn(grades);

        List<Grade> result = gradeService.getGradesByUserId(testUser.getId());

        assertEquals(grades, result);
        assertEquals(1, result.size());
        verify(gradeRepository).findByUserId(testUser.getId());
    }

    @Test
    void calculateAverageForTest() {
        double avgGrade = 85.5;
        when(gradeRepository.findAverageGradeByTestId(testEntity.getId())).thenReturn(Optional.of(avgGrade));

        double result = gradeService.calculateAverageForTest(testEntity.getId());

        assertEquals(avgGrade, result);
        verify(gradeRepository).findAverageGradeByTestId(testEntity.getId());
    }

    @Test
    void calculateAverageForTestNoGrades() {
        when(gradeRepository.findAverageGradeByTestId(testEntity.getId())).thenReturn(Optional.empty());

        double result = gradeService.calculateAverageForTest(testEntity.getId());

        assertEquals(0.0, result);
        verify(gradeRepository).findAverageGradeByTestId(testEntity.getId());
    }

    @Test
    void getAllGrades() {
        List<Grade> grades = Arrays.asList(testGrade, new Grade());
        when(gradeRepository.findAll()).thenReturn(grades);

        List<Grade> result = gradeService.getAllGrades();

        assertEquals(grades, result);
        assertEquals(2, result.size());
        verify(gradeRepository).findAll();
    }
}