package com.backend.service;

import com.backend.dto.TestDTO;
import com.backend.mapper.TestMapper;
import com.backend.model.Course;
import com.backend.model.TestEntity;
import com.backend.model.User;
import com.backend.repository.TestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private TestMapper testMapper;

    @InjectMocks
    private TestService testService;

    private TestEntity testEntity;
    private TestDTO testDTO;
    private User professor;
    private Course course;
    private Date testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new User();
        professor.setId(1);
        professor.setEmail("professor@example.com");

        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");

        testDate = new Date();

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setTitle("Test Exam");
        testEntity.setDescription("Test Description");
        testEntity.setDate(testDate);
        testEntity.setProfessor(professor);
        testEntity.setCourse(course);
        testEntity.setQuestions(new ArrayList<>());

        testDTO = new TestDTO();
        testDTO.setId(1L);
        testDTO.setTitle("Test Exam");
        testDTO.setDescription("Test Description");
        testDTO.setDate(testDate);
        testDTO.setProfessorId(professor.getId());
        testDTO.setCourseId(course.getId());
    }

    @Test
    void shouldSaveTestDTO() {
        when(testMapper.toEntity(testDTO)).thenReturn(testEntity);
        when(testRepository.save(testEntity)).thenReturn(testEntity);
        when(testMapper.toDTO(testEntity)).thenReturn(testDTO);

        TestDTO result = testService.saveTest(testDTO);

        assertEquals(testDTO, result);
        verify(testRepository).save(testEntity);
        verify(testMapper).toEntity(testDTO);
        verify(testMapper).toDTO(testEntity);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTest() {
        assertThrows(IllegalArgumentException.class, () -> testService.saveTest(null));
    }

    @Test
    void shouldCreateTestDTO() {
        TestDTO newTestDTO = new TestDTO();
        newTestDTO.setTitle("New Test");
        newTestDTO.setDescription("New Description");
        newTestDTO.setDate(testDate);
        newTestDTO.setProfessorId(professor.getId());
        newTestDTO.setCourseId(course.getId());

        TestEntity newTestEntity = new TestEntity();
        newTestEntity.setTitle("New Test");
        newTestEntity.setDescription("New Description");
        newTestEntity.setDate(testDate);
        newTestEntity.setProfessor(professor);
        newTestEntity.setCourse(course);

        when(testMapper.toEntity(newTestDTO)).thenReturn(newTestEntity);
        when(testRepository.save(any(TestEntity.class))).thenReturn(newTestEntity);
        when(testMapper.toDTO(newTestEntity)).thenReturn(newTestDTO);

        TestDTO result = testService.createTest(newTestDTO);

        assertEquals(newTestDTO, result);
        verify(testRepository).save(newTestEntity);
    }

    @Test
    void shouldThrowExceptionWhenCreatingTestWithId() {
        TestDTO invalidTestDTO = new TestDTO();
        invalidTestDTO.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> testService.createTest(invalidTestDTO));
    }

    @Test
    void shouldGetAllTestsDTO() {
        when(testRepository.findAll()).thenReturn(List.of(testEntity));
        when(testMapper.toDTO(testEntity)).thenReturn(testDTO);

        List<TestDTO>  result = (List<TestDTO>) testService.getAllTests();

        assertEquals(1, result.size());
        assertEquals(testDTO, result.get(0));
        verify(testRepository).findAll();
    }

    @Test
    void shouldGetTestByIdDTO() {
        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(testMapper.toDTO(testEntity)).thenReturn(testDTO);

        var result = testService.getTestById(1L);

        assertEquals(testDTO, result);
        verify(testRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTestNotFound() {
        when(testRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(999L));
    }

    @Test
    void shouldThrowExceptionWhenInvalidTestId() {
        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(0L));
        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(null));
    }

    @Test
    void shouldDeleteTestById() {
        when(testRepository.existsById(1L)).thenReturn(true);
        doNothing().when(testRepository).deleteById(1L);

        testService.deleteTestById(1L);

        verify(testRepository).existsById(1L);
        verify(testRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTest() {
        when(testRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testService.deleteTestById(999L));
    }

    @Test
    void shouldThrowExceptionWhenDeletingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> testService.deleteTestById(0L));
        assertThrows(IllegalArgumentException.class, () -> testService.deleteTestById(null));
    }

    @Test
    void shouldUpdateTestDTO() {
        TestDTO updateDTO = new TestDTO();
        updateDTO.setTitle("Updated Test");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDate(testDate);
        updateDTO.setProfessorId(professor.getId());
        updateDTO.setCourseId(course.getId());

        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(testRepository.save(any(TestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(testMapper.toDTO(any(TestEntity.class))).thenAnswer(invocation -> {
            TestEntity entity = invocation.getArgument(0);
            TestDTO dto = new TestDTO();
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setDate(entity.getDate());
            dto.setProfessorId(entity.getProfessor().getId());
            dto.setCourseId(entity.getCourse().getId());
            return dto;
        });

        TestDTO result = testService.updateTest(1L, updateDTO);

        ArgumentCaptor<TestEntity> captor = ArgumentCaptor.forClass(TestEntity.class);
        verify(testRepository).save(captor.capture());
        TestEntity savedEntity = captor.getValue();

        assertEquals(updateDTO.getTitle(), savedEntity.getTitle());
        assertEquals(updateDTO.getDescription(), savedEntity.getDescription());
        assertEquals(updateDTO.getDate(), savedEntity.getDate());
        assertEquals(updateDTO.getProfessorId(), savedEntity.getProfessor().getId());
        assertEquals(updateDTO.getCourseId(), savedEntity.getCourse().getId());

        assertEquals(updateDTO.getTitle(), result.getTitle());
        assertEquals(updateDTO.getDescription(), result.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTest() {
        when(testRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testService.updateTest(999L, new TestDTO()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(0L, new TestDTO()));
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(null, new TestDTO()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullData() {

        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(1L, null));
    }
}
