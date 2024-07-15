package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findAll(Specification<Category> spec, Pageable pageable);
}
