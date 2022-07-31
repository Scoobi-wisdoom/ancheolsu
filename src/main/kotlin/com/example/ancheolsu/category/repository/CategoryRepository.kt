package com.example.ancheolsu.category.repository

import com.example.ancheolsu.category.entity.Category
import com.example.ancheolsu.category.entity.CategoryId
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, CategoryId> {
    fun findByCategoryName(categoryName: String): Category?
}
