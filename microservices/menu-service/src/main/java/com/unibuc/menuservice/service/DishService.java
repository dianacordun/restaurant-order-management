package com.unibuc.menuservice.service;

import com.unibuc.menuservice.dto.DishCreateDTO;
import com.unibuc.menuservice.dto.DishDTO;
import com.unibuc.menuservice.dto.IngredientDTO;
import com.unibuc.menuservice.model.Dish;
import com.unibuc.menuservice.model.Ingredient;
import com.unibuc.menuservice.repository.DishRepository;
import com.unibuc.menuservice.repository.IngredientRepository;
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
        return dishes.stream().map(this::convertToDishDTO).collect(Collectors.toList());
    }

    public List<DishDTO> getAllAvailableDishes() {
        List<Dish> dishes = dishRepository.findByAvailableTrue();
        return dishes.stream().map(this::convertToDishDTO).collect(Collectors.toList());
    }

    public DishDTO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
        return convertToDishDTO(dish);
    }

    public DishDTO addDish(DishCreateDTO request) {
        // Validate ingredients exist
        List<Ingredient> ingredients = ingredientRepository.findByIdIn(request.getIngredientIds());

        if (ingredients.isEmpty()) {
            throw new RuntimeException("No valid ingredients found for the given IDs.");
        }
        if (ingredients.size() != request.getIngredientIds().size()) {
            throw new RuntimeException("Some ingredients were not found.");
        }

        // Create new dish
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setPrice(request.getPrice());
        dish.setIngredients(ingredients);
        dish.setAvailable(true); // New dishes are available by default

        Dish savedDish = dishRepository.save(dish);
        return convertToDishDTO(savedDish);
    }

    public DishDTO updateDishAvailability(Long id, Boolean availability) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));

        dish.setAvailable(availability);
        Dish updatedDish = dishRepository.save(dish);
        return convertToDishDTO(updatedDish);
    }

    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new RuntimeException("Dish not found with id: " + id);
        }
        dishRepository.deleteById(id);
    }

    private DishDTO convertToDishDTO(Dish dish) {
        List<IngredientDTO> ingredientDTOs = dish.getIngredients().stream()
                .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getStock()))
                .collect(Collectors.toList());

        return new DishDTO(dish.getId(), dish.getName(), dish.getPrice(), dish.isAvailable(), ingredientDTOs);
    }
}
