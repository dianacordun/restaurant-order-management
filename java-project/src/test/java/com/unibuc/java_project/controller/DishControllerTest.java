package com.unibuc.java_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.java_project.dto.DishCreateDTO;
import com.unibuc.java_project.dto.DishDTO;
import com.unibuc.java_project.dto.IngredientForDishDTO;
import com.unibuc.java_project.model.Dish;
import com.unibuc.java_project.model.Ingredient;
import com.unibuc.java_project.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DishController.class)
@ExtendWith(MockitoExtension.class)
public class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();
    }

    @Test
    void testGetAllDishes() throws Exception {
        // Given
        List<DishDTO> dishDTOList = Arrays.asList(
                new DishDTO(1L, "Margherita Pizza", 12.99, true, Arrays.asList(new IngredientForDishDTO(1L, "Tomato"), new IngredientForDishDTO(2L, "Cheese"))),
                new DishDTO(2L, "Diavola", 15.99, true, Arrays.asList(new IngredientForDishDTO(1L, "Tomato"), new IngredientForDishDTO(3L, "Basil")))
        );

        when(dishService.getAllDishes()).thenReturn(dishDTOList);

        // When & Then
        mockMvc.perform(get("/api/dishes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Margherita Pizza"))
                .andExpect(jsonPath("$[1].name").value("Diavola"));
    }

    @Test
    void testGetAllAvailableDishes() throws Exception {
        // Given
        List<DishDTO> availableDishes = Arrays.asList(
                new DishDTO(1L, "Margherita Pizza", 12.99, true, Arrays.asList(new IngredientForDishDTO(1L, "Tomato"), new IngredientForDishDTO(2L, "Cheese"))),
                new DishDTO(2L, "Diavola", 15.99, true, Arrays.asList(new IngredientForDishDTO(1L, "Tomato"), new IngredientForDishDTO(3L, "Basil")))
        );

        when(dishService.getAllAvailableDishes()).thenReturn(availableDishes);

        // When & Then
        mockMvc.perform(get("/api/dishes/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Margherita Pizza"))
                .andExpect(jsonPath("$[1].name").value("Diavola"));
    }

    @Test
    void testAddDish() throws Exception {
        // Given
        DishCreateDTO dishCreateDTO = new DishCreateDTO(Arrays.asList(1L, 2L, 3L),10.99, "Caprese Salad");

        Ingredient ingredient1 = new Ingredient(1L, "Tomato");
        Ingredient ingredient2 = new Ingredient(2L, "Cheese");
        Ingredient ingredient3 = new Ingredient(3L, "Basil");

        Dish newDish = new Dish(3L, "Caprese Salad", 10.99, true, Arrays.asList(ingredient1, ingredient2, ingredient3));

        when(dishService.addDish(dishCreateDTO)).thenReturn(newDish);

        // When & Then
        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dish successfully added."));
    }

    @Test
    void testUpdateDishAvailability() throws Exception {
        // Given
        Long dishId = 1L;
        Boolean availability = false;
        Ingredient ingredient1 = new Ingredient(1L, "Tomato");
        Ingredient ingredient2 = new Ingredient(2L, "Cheese");

        Dish updatedDish = new Dish(1L, "Margherita Pizza", 12.99, false, Arrays.asList(ingredient1, ingredient2));

        when(dishService.updateDishAvailability(dishId, availability)).thenReturn(updatedDish);

        // When & Then
        mockMvc.perform(patch("/api/dishes/{id}", dishId)
                        .param("availability", availability.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Dish successfully updated."));
    }

    @Test
    void testDeleteDish() throws Exception {
        // Given
        Long dishId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/dishes/{id}", dishId))
                .andExpect(status().isNoContent());

        verify(dishService).deleteDish(dishId);  // Verifying that the delete method in the service was called
    }
}
