package com.unibuc.menuservice.service;

import com.unibuc.menuservice.dto.IngredientDTO;
import com.unibuc.menuservice.model.Ingredient;
import com.unibuc.menuservice.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<IngredientDTO> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public IngredientDTO getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
        return convertToDTO(ingredient);
    }

    public IngredientDTO addIngredient(IngredientDTO ingredientDTO) {
        if (ingredientRepository.existsByName(ingredientDTO.getName())) {
            throw new RuntimeException("Ingredient with name '" + ingredientDTO.getName() + "' already exists");
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        ingredient.setStock(ingredientDTO.getStock());

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return convertToDTO(savedIngredient);
    }

    public IngredientDTO updateIngredient(Long id, IngredientDTO ingredientDTO) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

        ingredient.setName(ingredientDTO.getName());
        ingredient.setStock(ingredientDTO.getStock());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return convertToDTO(updatedIngredient);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    public IngredientDTO updateStock(Long id, Integer newStock) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

        ingredient.setStock(newStock);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return convertToDTO(updatedIngredient);
    }

    private IngredientDTO convertToDTO(Ingredient ingredient) {
        return new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getStock());
    }
}
