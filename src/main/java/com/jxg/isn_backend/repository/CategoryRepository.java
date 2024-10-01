package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.model.Category;
import com.jxg.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
