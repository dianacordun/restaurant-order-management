package com.unibuc.menuservice.repository;

import com.unibuc.menuservice.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByAvailableTrue();

    @Query("SELECT d FROM Dish d WHERE d.available = true")
    List<Dish> findAllAvailableDishes();

    boolean existsByName(String name);
}
