package com.backend.service;

import com.backend.dto.MaterialDTO;
import com.backend.model.Course;
import com.backend.model.Material;
import com.backend.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    private MaterialRepository repository;
    private MaterialService service;
    private CourseService courseService;

    private Material testMaterial;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        repository = mock(MaterialRepository.class);
        courseService = mock(CourseService.class);

        // Initialize the service with mocks
        service = new MaterialService(repository, courseService);

        // Create test data
        testCourse = new Course();
        testCourse.setId(1L);

        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setName("Test Material");
        testMaterial.setPath("/test/path");
        testMaterial.setCourse(testCourse);
    }

    @Test
    void testGetAllMaterials() {
        // Mock behavior
        when(repository.findAll()).thenReturn(List.of(testMaterial));

        // Call the service method
        List<Material> result = service.getAllMaterials();

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial, result.get(0));

        // Verify repository interaction
        verify(repository).findAll();
    }

    @Test
    void testGetMaterialById_Found() {
        // Mock behavior
        when(repository.findById(1L)).thenReturn(Optional.of(testMaterial));

        // Call the service method
        Material result = service.getMaterialById(1L);

        // Assertions
        assertNotNull(result);
        assertEquals("Test Material", result.getName());
        assertEquals("/test/path", result.getPath());

        // Verify repository interaction
        verify(repository).findById(1L);
    }

    @Test
    void testGetMaterialById_NotFound() {
        // Mock behavior
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getMaterialById(1L));
        assertTrue(exception.getMessage().contains("Material not found with id: 1"));

        // Verify repository interaction
        verify(repository).findById(1L);
    }

    @Test
    void testCreateMaterial_Success() {
        // Create test DTO
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setName("Test Material");
        materialDTO.setPath("/test/path");
        materialDTO.setCourseId(1L);

        // Mock behavior
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(testCourse));
        when(repository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Material result = service.createMaterial(materialDTO);

        // Assertions
        assertNotNull(result);
        assertEquals("Test Material", result.getName());
        assertEquals("/test/path", result.getPath());
        assertEquals(1L, result.getCourse().getId());

        // Verify interactions
        verify(courseService).getCourseById(1L);
        verify(repository).save(any(Material.class));
    }

    @Test
    void testCreateMaterial_ThrowsExceptionForNullCourse() {
        // Create test DTO with invalid courseId
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setName("Test Material");
        materialDTO.setPath("/test/path");
        materialDTO.setCourseId(null);

        // Call the service method and assert exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createMaterial(materialDTO));
        assertEquals("Course ID cannot be null.", exception.getMessage());

        // Verify no interactions with repository or courseService
        verifyNoInteractions(courseService, repository);
    }

    @Test
    void testCreateMaterial_ThrowsExceptionForMissingName() {
        // Create test DTO with invalid name
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setName(null); // Invalid name
        materialDTO.setPath("/test/path");
        materialDTO.setCourseId(1L);

        // Call the service method and assert exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createMaterial(materialDTO));
        assertEquals("Material name cannot be empty.", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(courseService, repository);
    }

    @Test
    void testUpdateMaterial_Found() {
        // Create an updated Material entity
        Material updatedMaterial = new Material();
        updatedMaterial.setName("Updated Material");
        updatedMaterial.setPath("/updated/path");
        updatedMaterial.setCourse(testCourse);

        // Mock behavior
        when(repository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(repository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Material result = service.updateMaterial(1L, updatedMaterial);

        // Assertions
        assertNotNull(result);
        assertEquals("Updated Material", result.getName());
        assertEquals("/updated/path", result.getPath());

        // Verify interactions
        verify(repository).findById(1L);
        verify(repository).save(testMaterial);
    }

    @Test
    void testUpdateMaterial_NotFound() {
        // Mock behavior
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateMaterial(1L, testMaterial));
        assertTrue(exception.getMessage().contains("Material not found with id: 1"));

        // Verify interactions
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteMaterial() {
        // Call the service method
        service.deleteMaterial(1L);

        // Verify interaction
        verify(repository).deleteById(1L);
    }

    @Test
    void testFindByCourseId() {
        // Mock behavior
        when(repository.findByCourseId(1L)).thenReturn(List.of(testMaterial));

        // Call the service method
        List<Material> result = service.findByCourseId(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial, result.get(0));

        // Verify interaction
        verify(repository).findByCourseId(1L);
    }

    @Test
    void testFindByNameContaining() {
        // Mock behavior
        when(repository.findByNameContaining("Material")).thenReturn(List.of(testMaterial));

        // Call the service method
        List<Material> result = service.findByNameContaining("Material");

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial, result.get(0));

        // Verify interaction
        verify(repository).findByNameContaining("Material");
    }

    @Test
    void testFindByPathContaining() {
        // Mock behavior
        when(repository.findByPathContaining("/test")).thenReturn(List.of(testMaterial));

        // Call the service method
        List<Material> result = service.findByPathContaining("/test");

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial, result.get(0));

        // Verify interaction
        verify(repository).findByPathContaining("/test");
    }
}