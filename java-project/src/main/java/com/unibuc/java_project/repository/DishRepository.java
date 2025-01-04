package com.unibuc.java_project.repository;

import com.unibuc.java_project.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE d.available = true")
    List<Dish> findAllAvailableDishes();

    Optional<Dish> findByName(String name);
}