package com.unibuc.java_project.repository;

import com.unibuc.java_project.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}

