package com.backend.controller;

import com.backend.model.Grade;
import com.backend.model.GradeId;
import com.backend.service.GradeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class GradeControllerTest {

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    public GradeControllerTest() {
        openMocks(this);
    }

    private Grade createSampleGrade() {
        Grade grade = new Grade();
        grade.setGrade(8.4f);
        grade.setId(new GradeId(1L,1));
        grade.setSubmissionDate(new java.sql.Date(System.currentTimeMillis()));
        return grade;
    }

    @Test
    void shouldReturnAllGrades() {
        List<Grade> grades = List.of(createSampleGrade());
        when(gradeService.getAllGrades()).thenReturn(grades);

        ResponseEntity<List<Grade>> response = gradeController.getAllGrades();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(grades, response.getBody());
        verify(gradeService).getAllGrades();
    }

    @Test
    void shouldAddGrade() {
        Grade grade = createSampleGrade();
        when(gradeService.addGrade(grade)).thenReturn(grade);

        ResponseEntity<Grade> response = gradeController.addGrade(grade);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(grade, response.getBody());
        verify(gradeService).addGrade(grade);
    }

    @Test
    void shouldReturnGradesByTestIdAndUserId() {
        Grade grade = createSampleGrade();
        when(gradeService.getGrade(1L,1)).thenReturn(Optional.of(grade));

        ResponseEntity<Grade> response = gradeController.getGrade(1L,1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(grade, response.getBody());
        verify(gradeService).getGrade(1L,1);
    }

    @Test
    void shouldReturnNotFoundForMissingGrade() {
        when(gradeService.getGrade(1L, 1)).thenReturn(Optional.empty());

        ResponseEntity<Grade> response = gradeController.getGrade(1L, 1);

        assertEquals(404, response.getStatusCodeValue());
        verify(gradeService).getGrade(1L, 1);
    }

    @Test
    void shouldUpdateGrade(){
        Grade grade = createSampleGrade();
        Grade updatedGrade = createSampleGrade();
        updatedGrade.setGrade(9.0f);
        when(gradeService.updateGrade(1L,1,updatedGrade)).thenReturn(Optional.of(updatedGrade));

        ResponseEntity<Grade> response = gradeController.updateGrade(1L,1,updatedGrade);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedGrade, response.getBody());
        verify(gradeService).updateGrade(1L,1,updatedGrade);
    }

    @Test
    void shouldDeleteGrade(){
        ResponseEntity<Void> response = gradeController.deleteGrade(1L,1);

        assertEquals(204, response.getStatusCodeValue());
        verify(gradeService).deleteGrade(1L,1);
    }

    @Test
    void shouldReturnGradesForUser(){
        List<Grade> grades = List.of(createSampleGrade());
        when(gradeService.getGradesByUserId(1)).thenReturn(grades);

        ResponseEntity<List<Grade>> response = gradeController.getGradesForUser(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(grades, response.getBody());
        verify(gradeService).getGradesByUserId(1);
    }

    @Test
    void shouldReturnAverageGradeForTest(){
        double average = 8.4;
        when(gradeService.calculateAverageForTest(1L)).thenReturn(average);

        ResponseEntity<Double> response = gradeController.getAverageGradeForTest(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(average, response.getBody());
        verify(gradeService).calculateAverageForTest(1L);
    }

}
