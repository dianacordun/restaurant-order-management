package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.DishCreateDTO;
import com.unibuc.java_project.dto.DishDTO;
import com.unibuc.java_project.dto.IngredientForDishDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.exceptions.UnavailableException;
import com.unibuc.java_project.model.Dish;
import com.unibuc.java_project.model.Ingredient;
import com.unibuc.java_project.repository.DishRepository;
import com.unibuc.java_project.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<DishDTO> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream().map(this::convertToDishDTO).collect(Collectors.toList()); // ??
    }

    public List<DishDTO> getAllAvailableDishes() {
        List<Dish> dishes = dishRepository.findAllAvailableDishes();
        return dishes.stream().map(this::convertToDishDTO).collect(Collectors.toList());
    }

    public Dish addDish(DishCreateDTO request) {
        List<Ingredient> ingredients = ingredientRepository.findAllById(request.getIngredientIds());

        if (ingredients.isEmpty()) {
            throw new UnavailableException("No valid ingredients found for the given IDs.");
        }
        if (ingredients.size() != request.getIngredientIds().size()) {
            throw new ResourceNotFoundException("Some ingredients were not found.");
        }

        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setPrice(request.getPrice());
        dish.setIngredients(ingredients);
        dish.setAvailable(true);

        return dishRepository.save(dish);
    }

    public Dish updateDishAvailability(Long id, Boolean availability) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        dish.setAvailable(availability);
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found");
        }
        dishRepository.deleteById(id);
    }

    private DishDTO convertToDishDTO(Dish dish) {
        List<IngredientForDishDTO> ingredients = dish.getIngredients().stream()
                .map(ingredient -> new IngredientForDishDTO(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());

        return new DishDTO(dish.getId(), dish.getName(), dish.getPrice(), dish.isAvailable(), ingredients);
    }
}
