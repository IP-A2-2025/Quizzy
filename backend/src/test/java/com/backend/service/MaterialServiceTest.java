// com/backend/service/MaterialServiceTest.java
package com.backend.service;

import com.backend.model.Course;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    private MaterialRepository repository;
    private MaterialService service;
    private Material material;

    @BeforeEach
    void setUp() {
        repository = mock(MaterialRepository.class);
        service = new MaterialService(repository);

        // A minimal Material instance
        Course course = new Course();
        course.setId(42L);
        material = new Material();
        material.setId(1L);
        material.setName("Test Material");
        material.setPath("/path/to/file.pdf");
        material.setCourse(course);
    }

    @Test
    void testGetAllMaterials() {
        when(repository.findAll()).thenReturn(List.of(material));

        List<Material> result = service.getAllMaterials();
        assertEquals(1, result.size());
        assertSame(material, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void testGetMaterialById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(material));

        Material result = service.getMaterialById(1L);
        assertSame(material, result);
        verify(repository).findById(1L);
    }

    @Test
    void testGetMaterialById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        var ex = assertThrows(RuntimeException.class, () -> service.getMaterialById(1L));
        assertTrue(ex.getMessage().contains("Material not found"));
    }

    @Test
    void testCreateMaterial() {
        when(repository.save(material)).thenReturn(material);

        Material result = service.createMaterial(material);
        assertSame(material, result);
        verify(repository).save(material);
    }

    @Test
    void testUpdateMaterial_Found() {
        Material updated = new Material();
        updated.setName("Updated");
        updated.setPath("/new.pdf");
        updated.setCourse(material.getCourse());

        when(repository.findById(1L)).thenReturn(Optional.of(material));
        when(repository.save(any(Material.class))).thenAnswer(inv -> inv.getArgument(0));

        Material result = service.updateMaterial(1L, updated);
        assertEquals("Updated", result.getName());
        assertEquals("/new.pdf", result.getPath());
        verify(repository).findById(1L);
        verify(repository).save(material);
    }

    @Test
    void testUpdateMaterial_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        var ex = assertThrows(RuntimeException.class, () -> service.updateMaterial(1L, material));
        assertTrue(ex.getMessage().contains("Material not found"));
    }

    @Test
    void testDeleteMaterial() {
        service.deleteMaterial(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testFindByCourseId() {
        when(repository.findByCourseId(42L)).thenReturn(List.of(material));
        var result = service.findByCourseId(42L);
        assertEquals(1, result.size());
        verify(repository).findByCourseId(42L);
    }

    @Test
    void testFindByNameContaining() {
        when(repository.findByNameContaining("Test")).thenReturn(List.of(material));
        var result = service.findByNameContaining("Test");
        assertEquals(1, result.size());
        verify(repository).findByNameContaining("Test");
    }

    // similar tests for findByPathContaining and findByProfessorId...
}
