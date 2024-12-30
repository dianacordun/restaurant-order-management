package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.IngredientDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.model.Ingredient;
import com.unibuc.java_project.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    // Fetch all ingredients as DTOs
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update stock for an ingredient
    public IngredientDTO updateStock(Long id, Integer stock) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + id + " not found."));
        ingredient.setStock(stock);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return mapToDTO(updatedIngredient);
    }

    // Helper method to map an Ingredient to IngredientDTO
    private IngredientDTO mapToDTO(Ingredient ingredient) {
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getStock()
        );
    }
}
