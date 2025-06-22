package com.unibuc.menuservice.repository;

import com.unibuc.menuservice.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByName(String name);

    List<Ingredient> findByStockGreaterThan(Integer stock);

    List<Ingredient> findByIdIn(List<Long> ids);
}
