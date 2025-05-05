package com.backend.controller;

import com.backend.model.Material;
import com.backend.service.MaterialService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MaterialControllerTest {

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    public MaterialControllerTest() {
        openMocks(this);
    }

    private Material createMaterial(Long id, String name, String path) {
        Material material = new Material();
        material.setId(id);
        material.setName(name);
        material.setPath(path);
        return material;
    }

    @Test
    void shouldReturnAllMaterials() {
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(materialService.getAllMaterials()).thenReturn(materials);

        ResponseEntity<List<Material>> response = materialController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnMaterialById() {
        Material material = createMaterial(1L, "Java Intro", "/java");
        when(materialService.getMaterialById(1L)).thenReturn(material);

        ResponseEntity<Material> response = materialController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Java Intro", response.getBody().getName());
    }

    @Test
    void shouldCreateMaterial() {
        Material material = createMaterial(null, "Java Basics", "/intro");
        Material created = createMaterial(1L, "Java Basics", "/intro");
        when(materialService.createMaterial(material)).thenReturn(created);

        ResponseEntity<Material> response = materialController.create(material);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateMaterial() {
        Material updated = createMaterial(1L, "Updated Name", "/updated");
        when(materialService.updateMaterial(eq(1L), any(Material.class))).thenReturn(updated);

        ResponseEntity<Material> response = materialController.update(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void shouldDeleteMaterial() {
        doNothing().when(materialService).deleteMaterial(1L);

        ResponseEntity<Void> response = materialController.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnMaterialsByCourse() {
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(materialService.findByCourseId(100L)).thenReturn(materials);

        ResponseEntity<List<Material>> response = materialController.getByCourse(100L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldSearchByName() {
        List<Material> results = List.of(createMaterial(1L, "Algorithms", "/algo"));
        when(materialService.findByNameContaining("Algo")).thenReturn(results);

        ResponseEntity<List<Material>> response = materialController.searchByName("Algo");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldSearchByPath() {
        List<Material> results = List.of(createMaterial(2L, "DB", "/db"));
        when(materialService.findByPathContaining("/db")).thenReturn(results);

        ResponseEntity<List<Material>> response = materialController.searchByPath("/db");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("/db", response.getBody().get(0).getPath());
    }

    @Test
    void shouldReturnMaterialsByProfessor() {
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(materialService.findByProfessorId(12)).thenReturn(materials);

        ResponseEntity<List<Material>> response = materialController.getByProfessor(12);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}
