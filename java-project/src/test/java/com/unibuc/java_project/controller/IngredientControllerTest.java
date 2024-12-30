package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.IngredientDTO;
import com.unibuc.java_project.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    private IngredientDTO ingredient1;
    private IngredientDTO ingredient2;

    @BeforeEach
    void setUp() {
        // Prepare test data
        ingredient1 = new IngredientDTO(1L, "Tomato", 50);
        ingredient2 = new IngredientDTO(2L, "Cheese", 30);
    }

    @Test
    void testGetAllIngredients() throws Exception {
        // Define mock behavior
        List<IngredientDTO> ingredientList = Arrays.asList(ingredient1, ingredient2);
        when(ingredientService.getAllIngredients()).thenReturn(ingredientList);

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // HTTP 200 status code
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Content type is JSON
                .andExpect(content().json("["
                        + "{\"id\":1,\"name\":\"Tomato\",\"stock\":50},"
                        + "{\"id\":2,\"name\":\"Cheese\",\"stock\":30}]"));  // Verify JSON response body
    }

    @Test
    void testUpdateStock() throws Exception {
        // Arrange
        Long ingredientId = 1L;
        int newStock = 60;
        IngredientDTO updatedIngredient = new IngredientDTO(ingredientId, "Tomato", newStock);
        when(ingredientService.updateStock(ingredientId, newStock)).thenReturn(updatedIngredient);

        // Act & Assert
        mockMvc.perform(patch("/api/ingredients/{id}/stock", ingredientId)
                        .param("stock", String.valueOf(newStock))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Tomato")))
                .andExpect(jsonPath("$.stock", is(60)));

        verify(ingredientService, times(1)).updateStock(ingredientId, newStock);
    }

    @Test
    void testUpdateStock_NotFound() throws Exception {
        // Arrange
        Long ingredientId = 1L;
        int newStock = 60;
        when(ingredientService.updateStock(ingredientId, newStock)).thenThrow(new RuntimeException("Ingredient not found"));

        // Act & Assert
        mockMvc.perform(patch("/api/ingredients/{id}/stock", ingredientId)
                        .param("stock", String.valueOf(newStock))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ingredientService, times(1)).updateStock(ingredientId, newStock);
    }
}